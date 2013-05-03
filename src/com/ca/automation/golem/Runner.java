/*
 */
package com.ca.automation.golem;

import com.ca.automation.golem.annotations.fields.RunConnection;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.context.actionInterfaces.RunCond;
import com.ca.automation.golem.context.actionInterfaces.RunConectionSpool;
import com.ca.automation.golem.context.actionInterfaces.RunConnectionFactory;
import com.ca.automation.golem.context.actionInterfaces.RunCycleContent;
import com.ca.automation.golem.interfaces.RunCycle;
import com.ca.automation.golem.context.actionInterfaces.SimpleParameterSpool;
import com.ca.automation.golem.context.actionInterfaces.managers.RunActionStackManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCycleManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunDelayIntervalManagerContext;
import com.ca.automation.golem.context.managers.RunActionStackManager;
import com.ca.automation.golem.context.managers.RunCycleManagerImpl;
import com.ca.automation.golem.context.managers.RunDelayIntervalManager;
import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.ContextManager;
import com.ca.automation.golem.interfaces.RunCycleManager;
import com.ca.automation.golem.spools.actions.ActionInformationSpool;
import com.ca.automation.golem.toRefactor.RunnerActiveStackList;
import com.ca.automation.golem.toRefactor.RunnerActiveTimerList;
import com.ca.automation.golem.toRefactor.RunnerConnectionFactoryImpl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    protected RunCond runConditions;
    protected RunContextImpl<Object, String, Object> run;

    /**
     * This is method for save runner creation in JDK environment.
     *
     * @return new instance of runner initialized by default action container
     */
    public static Runner createNew() {
        @SuppressWarnings("UseInjectionInsteadOfInstantion")
        Runner retValue = new Runner();
        retValue.actionData = ActionInformationSpool.getDefaultInstance();
        return retValue;
    }

    public boolean run(ActionStream<Object,String,Object> actions) {
        boolean retValue = false;
        run.setActionStream(actions);
        Map<String, Object> runParameterMap = actions.getParameterMap();
        for (Object o : run) {

            // TODO store return value for dealing with failed action into context 
            //      where must be implemented functionality for it
            runAction(o, runParameterMap);
            
            // TODO finilize work on error state handling and remove this part
            if (this.runConditions.getCentralStop()) {
                break;
            }
        }

        return retValue;
    }

//    /**
//     * This methods executes the root element of the tree and all branches in
//     * the tree.
//     *
//     * @param root
//     * @return true if operation ended successfully
//     */
//    public boolean run(RunTreeElement root) {
//        return run(root, null);
//    }
//
//    /**
//     *
//     * @param root
//     * @param runDataMap
//     * @return
//     */
//    public boolean run(RunTreeElement root, RunParameterMap runDataMap) {
//        boolean retValue = true;
//        this.runConditions = new RunCondImpl();
//        this.connSpool = new RunnerConnectionSpool();
//        this.run = new RunContextImpl<Object, String, Object>();
////        this.run.setRootElement(root);
//
//        if (runDataMap == null) {
//            runDataMap = new RunnerParameterMapImpl();
//        }
//
////        Map<String, Object> runMap = traverseRunTree(root, runDataMap);
////        if ((runMap == null) || (runConditions.getCentralStop())) {
////            retValue = false;
////        }
//        return retValue;
//    }

//    private RunParameterMap traverseRunTree(RunTreeElement curreElement, RunParameterMap runParameterMap) {
////        this.run.setCurrentElement(curreElement);
//
//        if (curreElement.getParameters() != null) {
//            for (Entry<String, Object> entry : curreElement.getParameters().entrySet()) {
//                if (this.runConditions.getCentralStop()) {
//                    break;
//                }
//                runParameterMap.put(entry.getKey(), entry.getValue());
//            }
//        }
//
//        if (curreElement.hasLeaves()) {
//            run.setSteps(curreElement.getLeaves());
//            for (Object o : run) {
//                if (this.runConditions.getCentralStop()) {
//                    break;
//                }
//                runParameterMap = runAction(o, runParameterMap);
//
//            }
//        } else if (curreElement.hasChildren()) {
//            for (RunTreeElement el : curreElement.getChildren()) {
//                if (this.runConditions.getCentralStop()) {
//                    break;
//                }
//                runParameterMap = traverseRunTree(el, runParameterMap);
//            }
//        }
//        return runParameterMap;
//    }

    protected boolean runAction(Object leaf, Map<String,Object> runParameterMap) {
        boolean retValue=false;
        if (actionData.isAction(leaf)) {
            injectParameters(leaf, runParameterMap);

            injectContexts(leaf, runParameterMap);

            injectConnections(leaf, runParameterMap);
            
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
                            Run run = m.getAnnotation(Run.class);
                            method = run.isCritical();
                        } else {
                            Validate valid = m.getAnnotation(Validate.class);
                            method = valid.isCritical();
                        }

                        if (method != null && method && !(Boolean) invoke) {
                            // TODO move this logick like part of refactoring reaction failed actions into context
                            runConditions.setCentralStop(true);
                            retValue = false;
                            break;
                        }
                    }

                    // TODO same after refactoring for reaction on failed action must be removed
                    if (runConditions.getCentralStop()) {
                        retValue = false;
                        break;
                    }
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, m.getName() + " in " + action.getClass().getSimpleName(), ex.getTargetException());
                }
            }
        }
        return retValue;
    }

    protected void retrieveRetValues(Object action, Map<String, Object> parmMap) {
        List<Field> retValues = actionData.getRetValues(action);
        if (retValues != null && !retValues.isEmpty()) {
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

    protected void injectConnections(Object action, Map<String, Object> parmMap) {
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
        if ((fields != null) && (!fields.isEmpty())) {
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

    protected void injectContexts(Object action, Map<String,Object> map) {
        List<Field> contexts = actionData.getContexts(action);
        if ((contexts != null) && (!contexts.isEmpty())) {
            for (Field f : contexts) {
                Class<?> type = f.getType();
                try {
                    if (type.isAssignableFrom(RunCond.class)) {
                        f.set(action, runConditions);
                    } else if (type.isAssignableFrom(SimpleParameterSpool.class)) {
                        f.set(action, map);
                    } else if (type.isAssignableFrom(RunConnectionFactory.class)) {
                        f.set(action, RunnerConnectionFactoryImpl.createNewFactory());
                    } else if (type.isAssignableFrom(RunConectionSpool.class)) {
                        f.set(action, connSpool);
                    } else if (type.isAssignableFrom(RunCycleManagerContext.class)) {
                        if (run.getCycleManager() == null) {
                            
                            ContextManager<Object, RunCycle<Object>,String,Object> tmp = new RunCycleManagerImpl<Object, String, Object>(run);
                            run.setCycleManager(tmp);
                        }
                        f.set(action, run.getCycleManager());
                    } else if (type.isAssignableFrom(RunCycleContent.class)) {
                        if (run.getCycleManager() == null) {
                            run.setCycleManager(new RunCycleManagerImpl<Object, String, Object>(run));
                        }
                        f.set(action, run.getCycleManager().getCurrent());
                    } else if (type.isAssignableFrom(RunDelayIntervalManagerContext.class)) {
                        if (run.getTimerManager() == null) {
                            run.setTimerManager(new RunDelayIntervalManager<Object, String, Object>(run));
                        }
                        f.set(action, run.getTimerManager());
                    } else if (type.isAssignableFrom(RunnerActiveTimerList.class)) {
                        if (run.getTimerManager() == null) {
                            run.setTimerManager(new RunDelayIntervalManager<Object, String, Object>(run));
                        }
                        // TODO Logic for accessing lists from actions 
//                        f.set(action, run.getTimerManager().getActiveTimers());
                    } else if (type.isAssignableFrom(RunActionStackManagerContext.class)) {
                        if (run.getStackManager() == null) {
                            run.setStackManager(new RunActionStackManager<Object, String, Object>(run));
                        }
                        f.set(action, run.getStackManager());
                    } else if (type.isAssignableFrom(RunnerActiveStackList.class)) {
                        if (run.getStackManager() == null) {
                            run.setStackManager(new RunActionStackManager<Object, String, Object>(run));
                        }
                        // TODO Logic for accessing lists from actions 
//                        f.set(action, run.getStackManager().getActiveStacks());
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
