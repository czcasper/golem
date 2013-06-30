/*
 */
package com.ca.automation.golem;

import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.context.actionInterfaces.RunConditionContext;
import com.ca.automation.golem.context.actionInterfaces.RunConditionsListContext;
import com.ca.automation.golem.context.actionInterfaces.RunConectionSpool;
import com.ca.automation.golem.context.actionInterfaces.RunConnectionFactory;
import com.ca.automation.golem.context.actionInterfaces.RunCycleContent;
import com.ca.automation.golem.context.actionInterfaces.RunDelaysListContext;
import com.ca.automation.golem.context.actionInterfaces.RunStacksListContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunActionStackManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCondManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCycleManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunDelayIntervalManagerContext;
import com.ca.automation.golem.context.actionInterfaces.spools.ParameterSpoolContext;
import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.connections.Connection;
import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.interfaces.context.managers.RunCondManager;
import com.ca.automation.golem.interfaces.context.managers.RunContextManagers;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.ActionInformationSpool;
import com.ca.automation.golem.interfaces.spools.ParameterSpool;
import com.ca.automation.golem.interfaces.spools.keys.ConnectionKey;
import com.ca.automation.golem.interfaces.spools.keys.ParameterKey;
import com.ca.automation.golem.spools.ActionInformationSpoolImpl;
import com.ca.automation.golem.spools.ParameterSpoolImpl;
import com.ca.automation.golem.spools.enums.ActionFieldProxyType;
import com.ca.automation.golem.spools.enums.ActionMethodProxyType;
import com.ca.automation.golem.toRefactor.RunnerConnectionFactoryImpl;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
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
    protected ActionInformationSpool<Object> actionData;
    protected AbstractSpool<Object, ConnectionKey, Connection> connSpool;
    protected RunContextImpl<Object, Boolean, Object> run;

    /**
     * This is method for save runner creation in JDK environment.
     *
     * @return new instance of runner initialized by default action container
     */
    public static Runner createNew() {
        @SuppressWarnings("UseInjectionInsteadOfInstantion")
        Runner retValue = new Runner();
        retValue.actionData = ActionInformationSpoolImpl.getGlobal();
        retValue.run = new RunContextImpl<>();
        return retValue;
    }

    public boolean run(ActionStream<Object, Object> actions) {
        boolean retValue = true;
        run.setActionStream(actions);
        ParameterSpool<Object,Object> runParameterMap = actions.getParameterMap();
        for (Object o : run) {
            boolean runAction = runAction(o, runParameterMap);
            if (!run.validateResult(runAction, true)) {
                retValue = false;
                break;
            }
        }

        return retValue;
    }

    public RunContextManagers<Object, Boolean, Object> getRunContext() {
        return run;
    }

    protected boolean runAction(Object leaf, ParameterSpool<Object,Object> runParameterMap) {
        boolean retValue = false;
        if (actionData.isValidAction(leaf)) {
            ActionInfoProxy proxy = actionData.get(leaf);
            injectParameters(leaf, proxy, runParameterMap);

            injectContexts(leaf, proxy, runParameterMap);

            injectConnections(leaf, proxy, runParameterMap);

            if (executeAction(leaf)) {
                retrieveRetValues(leaf, proxy, runParameterMap);
                retValue = true;
            }
        }

        return retValue;
    }

    protected boolean executeAction(Object action) {
        boolean retValue = true;
        ActionInfoProxy tmp = actionData.get(action);
        for (ActionMethodProxyType type : ActionMethodProxyType.values()) {
            if (!executeActionMethodsGroup(type,tmp.getMethod(type), action)) {
                retValue = false;
                break;
            }
        }
        return retValue;
    }

    protected boolean executeActionMethodsGroup(ActionMethodProxyType type, Collection<Method> actionMethods, Object action) {
        boolean retValue = true;
        if ((actionMethods != null) && (!actionMethods.isEmpty())) {
            for (Method m : actionMethods) {
                try {
                    Object invoke = m.invoke(action);
                    if ((invoke != null) && (invoke instanceof Boolean)) {
                        Annotation annotation = m.getAnnotation(type.getAnnotation());
                        boolean critical = type.isCritical(annotation);
                        if (critical) {
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

    protected void retrieveRetValues(Object action, ActionInfoProxy proxy, ParameterSpool<Object,Object> parmMap) {
        List<Field> retValues = proxy.getField(ActionFieldProxyType.ReturnValues);
        if (retValues != null && !retValues.isEmpty()) {
            if (parmMap == null) {
                parmMap = new ParameterSpoolImpl<Object, Object>();
            }

            for (Field f : retValues) {
                try {
                    parmMap.put(action, f, parmMap);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void injectConnections(Object action, ActionInfoProxy proxy, ParameterSpool<Object,Object> parmMap) {
        List<Field> connections = proxy.getField(ActionFieldProxyType.Connections);
        if ((connections != null) && (!connections.isEmpty())) {
            for (Field f : connections) {
                try {
                    Connection connection = connSpool.get(action, f, parmMap);
                    if (connection != null) {
                        Class<?> type = f.getType();
                        if (type.isAssignableFrom(connection.getClass())) {
                            try {
                                f.set(action, connection);
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            Logger.getLogger(Runner.class.getName()).log(Level.FINE, "Field:{0} in action:{1} request type:{2} which is not compatible with type of stored connection:{3}", new Object[]{f.getName(), action.getClass().getName(), type.getName(), connection.getClass().getName()});
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

    protected void injectParameters(Object action, ActionInfoProxy proxy, ParameterSpool<Object,Object> parmMap) {
        Collection<Field> fields = proxy.getField(ActionFieldProxyType.Parameters);
        if ((fields != null) && (!fields.isEmpty()) && (parmMap != null) && (!parmMap.isEmpty())) {
            for (Field f : fields) {
                try {
                    Object parametr = parmMap.get(action, f, parmMap);
                    if (f.getType().isAssignableFrom(parametr.getClass())) {
                        f.set(action, parametr);
                    }
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    protected void injectContexts(Object action, ActionInfoProxy proxy, Map<ParameterKey<?>, Object> parmMap) {
        List<Field> contexts = proxy.getField(ActionFieldProxyType.Contexts);
        if ((contexts != null) && (!contexts.isEmpty())) {
            for (Field f : contexts) {
                Class<?> type = f.getType();
                try {
                    if (type.isAssignableFrom(RunCondManagerContext.class)) {
                        f.set(action, run.getInitializedConditionManager());
                    } else if (type.isAssignableFrom(RunConditionContext.class)) {
                        RunCondManager<Object, Boolean, Object> man = run.getInitializedConditionManager();
                        f.set(action, man.getCurrent());
                    } else if (type.isAssignableFrom(RunConditionsListContext.class)) {
                        RunCondManager<Object, Boolean, Object> man = run.getInitializedConditionManager();
                        f.set(action, man.getActive());

                    } else if (type.isAssignableFrom(ParameterSpoolContext.class)) {
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
