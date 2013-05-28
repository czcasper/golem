/*
 */
package com.ca.automation.golem;

import com.ca.automation.golem.annotations.fields.RunConnection;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.context.actionInterfaces.RunConditionContext;
import com.ca.automation.golem.context.actionInterfaces.RunConditionsListContext;
import com.ca.automation.golem.context.actionInterfaces.RunConectionSpool;
import com.ca.automation.golem.context.actionInterfaces.RunConnectionFactory;
import com.ca.automation.golem.context.actionInterfaces.RunCycleContent;
import com.ca.automation.golem.context.actionInterfaces.RunDelaysListContext;
import com.ca.automation.golem.context.actionInterfaces.RunStacksListContext;
import com.ca.automation.golem.context.actionInterfaces.SimpleParameterSpool;
import com.ca.automation.golem.context.actionInterfaces.managers.RunActionStackManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCondManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCycleManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunDelayIntervalManagerContext;
import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.context.managers.RunCondManager;
import com.ca.automation.golem.interfaces.context.managers.RunContextManagers;
import com.ca.automation.golem.spools.actions.ActionInformationSpool;
import com.ca.automation.golem.toRefactor.RunnerConnectionFactoryImpl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author maslu02
 */
@Stateless
@LocalBean
public class Runner {

    @EJB
    protected ActionInformationSpool actionData;
    protected RunConectionSpool connSpool;
    protected RunContextImpl<Object, Boolean, String, Object> run;

    /**
     * This is method for save runner creation in JDK environment.
     *
     * @return new instance of runner initialized by default action container
     */
    public static Runner createNew() {
        @SuppressWarnings("UseInjectionInsteadOfInstantion")
        Runner retValue = new Runner();
        retValue.actionData = ActionInformationSpool.getDefaultInstance();
        retValue.run = new RunContextImpl<Object, Boolean, String, Object>();
        return retValue;
    }

    public boolean run(ActionStream<Object, String, Object> actions) {
        boolean retValue = true;
        run.setActionStream(actions);
        Map<String, Object> runParameterMap = actions.getParameterMap();
        for (Object o : run) {
            boolean runAction = runAction(o, runParameterMap);
            if (!run.validateResult(runAction, true)) {
                retValue = false;
                break;
            }
        }

        return retValue;
    }

    public RunContextManagers<Object, Boolean, String, Object> getRunContext() {
        return run;
    }

    protected boolean runAction(Object leaf, Map<String, Object> runParameterMap) {
        boolean retValue = false;
        if (actionData.isAction(leaf)) {
            injectParameters(leaf, runParameterMap);

            injectContexts(leaf, runParameterMap);

            injectConnections(leaf);

            if (executeAction(leaf)) {
                retrieveRetValues(leaf, runParameterMap);
                retValue = true;
            }
        }

        return retValue;
    }

    protected boolean executeAction(Object action) {
        boolean retValue = false;

        if ((executeActionMethodsGroup(actionData.getInits(action), action))
                && (executeActionMethodsGroup(actionData.getRuns(action), action))
                && (executeActionMethodsGroup(actionData.getValidates(action), action))) {
            retValue = true;
        }
        return retValue;
    }

    protected boolean executeActionMethodsGroup(List<Method> actionMethods, Object action) {
        boolean retValue = true;
        if ((actionMethods != null) && (!actionMethods.isEmpty())) {
            for (Method m : actionMethods) {
                try {
                    Object invoke = m.invoke(action);
                    if ((invoke != null) && (invoke instanceof Boolean)) {
                        Boolean method;
                        if (m.isAnnotationPresent(Init.class)) {
                            Init init = m.getAnnotation(Init.class);
                            method = init.isCritical();
                        } else if (m.isAnnotationPresent(Run.class)) {
                            Run criticalFlag = m.getAnnotation(Run.class);
                            method = criticalFlag.isCritical();
                        } else {
                            Validate valid = m.getAnnotation(Validate.class);
                            method = valid.isCritical();
                        }

                        if (method != null && method) {
                            if (run.validateResult((Boolean) invoke, false)) {
                                retValue = false;
                                break;
                            }
                        }
                    }

                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    // TODO handling exeption from action by  runner. Disaster recovery scenerions
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, m.getName() + " in " + action.getClass().getSimpleName(), ex.getTargetException());
                }
            }
        }
        return retValue;
    }

    protected void retrieveRetValues(Object action, Map<String, Object> parmMap) {
        List<Field> retValues = actionData.getRetValues(action);
        if (retValues != null && !retValues.isEmpty()) {
            if (parmMap == null) {
                parmMap = new LinkedHashMap<String, Object>(retValues.size(), 0.75f, true);
            }
            for (Field f : retValues) {
                try {
                    RunParameter annotation = f.getAnnotation(RunParameter.class);
                    String name = annotation.name();
                    if (name.isEmpty()) {
                        name = action.getClass().getSimpleName() + "." + f.getName();
                    }
                    Object value = f.get(action);
                    parmMap.put(name, value);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void injectConnections(Object action) {
        List<Field> connections = actionData.getConnections(action);
        if ((connections != null) && (!connections.isEmpty())) {
            for (Field f : connections) {
                Class<?> type = f.getType();
                if (type.isAssignableFrom(com.ca.automation.golem.context.actionInterfaces.RunConnection.class)) {
                    RunConnection connection = f.getAnnotation(RunConnection.class);
                    Object connectionKey = null;
                    if (!connection.key().isEmpty()) {
                        Field fieldByName = actionData.getFieldByName(action, connection.key());
                        if (fieldByName != null) {
                            try {
                                connectionKey = fieldByName.get(action);
                                if (!connSpool.containsKey(connectionKey)) {
                                    connectionKey = null;
                                }
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            Logger.getLogger(Runner.class.getName()).log(Level.WARNING, "Incorect connection key attribute:{0}. Object {1} have not defined field with this name ", new Object[]{connection.key(), action.getClass().getName()});
                        }
                    }
                    if (connectionKey == null) {
                        if (connection.name().isEmpty()) {
                            connectionKey = action.getClass().getSimpleName() + "." + f.getName();
                        } else {
                            connectionKey = connection.name();
                        }
                    }

                    if ((connectionKey != null) && (connSpool.containsKey(connectionKey))) {
                        try {
                            f.set(action, connSpool.get(connectionKey));
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            }
        }

    }

    protected void injectParameters(Object action, Map<String, Object> parmMap) {
        List<Field> fields = actionData.getFields(action);
        if ((fields != null) && (!fields.isEmpty()) && (parmMap != null) && (!parmMap.isEmpty())) {
            for (Field f : fields) {
                try {
                    RunParameter annotation = f.getAnnotation(RunParameter.class);
                    String name = annotation.name();
                    if (name.isEmpty()) {
                        name = action.getClass().getSimpleName() + "." + f.getName();
                    }

                    if (parmMap.containsKey(name)) {
                        Object value = parmMap.get(name);
                        Class<?> type = f.getType();

                        if ((value == null) || (type.isAssignableFrom(value.getClass())) || (isAssignableToPrimitive(type, value))) {
                            f.set(action, value);
                        } else {
                            Logger.getLogger(Runner.class.getName()).log(Level.WARNING, "Incompatible argument type in field {0}. Expected: {1} \". Found: {2} \"",
                                    new Object[]{f.getName(), f.getType().getName(), value.getClass().getName()});
                        }
                    }


                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    protected void injectContexts(Object action, Map<String, Object> parmMap) {
        List<Field> contexts = actionData.getContexts(action);
        if ((contexts != null) && (!contexts.isEmpty())) {
            for (Field f : contexts) {
                Class<?> type = f.getType();
                try {
                    if (type.isAssignableFrom(RunCondManagerContext.class)) {
                        f.set(action, run.getInitializedConditionManager());
                    } else if (type.isAssignableFrom(RunConditionContext.class)) {
                        RunCondManager<Object, Boolean, String, Object> man = run.getInitializedConditionManager();
                        f.set(action, man.getCurrent());
                    } else if (type.isAssignableFrom(RunConditionsListContext.class)) {
                        RunCondManager<Object, Boolean, String, Object> man = run.getInitializedConditionManager();
                        f.set(action, man.getActive());

                    } else if (type.isAssignableFrom(SimpleParameterSpool.class)) {
                        f.set(action, parmMap);
                    } else if (type.isAssignableFrom(RunConnectionFactory.class)) {
                        f.set(action, RunnerConnectionFactoryImpl.createNewFactory());
                    } else if (type.isAssignableFrom(RunConectionSpool.class)) {
                        f.set(action, connSpool);
                    } else if (type.isAssignableFrom(RunCycleManagerContext.class)) {
                        f.set(action, run.getInitializedCycleManager());
                    } else if (type.isAssignableFrom(RunCycleContent.class)) {
                        f.set(action, run.getInitializedCycleManager().getCurrent());
                    } else if (type.isAssignableFrom(RunDelayIntervalManagerContext.class)) {
                        f.set(action, run.getInitializedDelayManager());
                    } else if (type.isAssignableFrom(RunDelaysListContext.class)) {
                        f.set(action, run.getInitializedDelayManager().getActive());
                    } else if (type.isAssignableFrom(RunActionStackManagerContext.class)) {
                        f.set(action, run.getInitializedStackManager());
                    } else if (type.isAssignableFrom(RunStacksListContext.class)) {
                        f.set(action, run.getInitializedStackManager().getActive());
                    }
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    private boolean isAssignableToPrimitive(Class<?> type, Object value) {
        boolean retValue = false;
        if ((type.isPrimitive()) && (value != null)) {
            if ((type.isAssignableFrom(Long.TYPE)) && (value.getClass().isAssignableFrom(Long.class))) {
                retValue = true;
            } else if ((type.isAssignableFrom(Integer.TYPE)) && (value.getClass().isAssignableFrom(Integer.class))) {
                retValue = true;
            } else if ((type.isAssignableFrom(Short.TYPE)) && (value.getClass().isAssignableFrom(Short.class))) {
                retValue = true;
            } else if ((type.isAssignableFrom(Byte.TYPE)) && (value.getClass().isAssignableFrom(Byte.class))) {
                retValue = true;
            } else if ((type.isAssignableFrom(Double.TYPE)) && (value.getClass().isAssignableFrom(Double.class))) {
                retValue = true;
            } else if ((type.isAssignableFrom(Float.TYPE)) && (value.getClass().isAssignableFrom(Float.class))) {
                retValue = true;
            } else if ((type.isAssignableFrom(Character.TYPE)) && (value.getClass().isAssignableFrom(Character.class))) {
                retValue = true;
            } else if ((type.isAssignableFrom(Boolean.TYPE)) && (value.getClass().isAssignableFrom(Boolean.class))) {
                retValue = true;
            }
        }
        return retValue;
    }
}
