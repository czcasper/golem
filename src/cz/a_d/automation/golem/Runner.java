/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem;

import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.context.actionInterfaces.RunConditionContext;
import cz.a_d.automation.golem.context.actionInterfaces.RunConditionsListContext;
import cz.a_d.automation.golem.context.actionInterfaces.spools.RunConectionSpool;
import cz.a_d.automation.golem.interfaces.ConnectionFactory;
import cz.a_d.automation.golem.context.actionInterfaces.RunCycleContent;
import cz.a_d.automation.golem.context.actionInterfaces.RunDelaysListContext;
import cz.a_d.automation.golem.context.actionInterfaces.RunStacksListContext;
import cz.a_d.automation.golem.context.actionInterfaces.managers.RunActionStackManagerContext;
import cz.a_d.automation.golem.context.actionInterfaces.managers.RunCondManagerContext;
import cz.a_d.automation.golem.context.actionInterfaces.managers.RunCycleManagerContext;
import cz.a_d.automation.golem.context.actionInterfaces.managers.RunDelayIntervalManagerContext;
import cz.a_d.automation.golem.context.actionInterfaces.spools.ParameterSpoolContext;
import cz.a_d.automation.golem.context.connections.GolemConnectionFactory;
import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.context.managers.RunCondManager;
import cz.a_d.automation.golem.interfaces.context.managers.RunContextManagers;
import cz.a_d.automation.golem.interfaces.spools.AbstractSpool;
import cz.a_d.automation.golem.interfaces.spools.ActionInformationSpool;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;
import cz.a_d.automation.golem.interfaces.spools.keys.ParameterKey;
import cz.a_d.automation.golem.spools.ActionInformationSpoolImpl;
import cz.a_d.automation.golem.spools.ParameterSpoolImpl;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.enums.ActionMethodProxyType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runner is main entry point for all features supported by Golem. It is realize execution of actions stream. Automatically loading managers
 * for particular features of execution requested by actions in action stream. Parametrized data context is managed by injecting parameters
 * from context into actions and updated by retrieving parameters from action marked to be return value from action into context.
 *
 * @author casper
 */
public class Runner {

    /**
     * Cache with information related to action classes. This is global instance used by all runner threads. Information about actions are
     * collected by using reflection and this approach leads to more effective reusing of same action in execution flow.
     */
    protected ActionInformationSpool<Object> actionData;

    /**
     * Pool of connections used by actions from action stream. This is used for storing connections in separate memory space, than context
     * parameters. This allow more simple clean up after action stream.
     */
    protected AbstractSpool<Object, ConnectionKey, Connection> connSpool;

    /**
     * Implementation of all features provided by Golem to control execution flow and other aspects of actions execution. This allows more
     * effective extensions of Golem by new features manages without changing logic for running single action in Runner.
     */
    protected RunContextImpl<Object, Boolean, Object> run;

    /**
     * Runner constructor initialize data used by actions from global action information spool also create new instance of run context
     * implementation. This all is need to successfully execute stream of action by Golem.
     */
    public Runner() {
        actionData = ActionInformationSpoolImpl.getGlobal();
        run = new RunContextImpl<>();
    }

    /**
     * Execute actions from stream with including result validation, flow control, sharing data between actions and all other features
     * provided by Golem.
     *
     * @param actions Must be stream which contains at least one action.
     * @return true in case when whole stream execution has been done successfully, otherwise false.
     */
    public boolean run(ActionStream<Object, Object> actions) {
        boolean retValue = true;
        run.setActionStream(actions);
        ParameterSpool<Object, Object> runParameterMap = actions.getParameterSpool();
        for (Object o : run) {
            boolean runAction = runAction(o, runParameterMap);
            if (!run.validateResult(runAction, true)) {
                retValue = false;
                break;
            }
        }

        return retValue;
    }

    /**
     * Getter to allow access to currently used run context implementation.
     *
     * @return Instance of context manager used by this runner. Never return null.
     */
    public RunContextManagers<Object, Boolean, Object> getRunContext() {
        return run;
    }

    /**
     * Validates if object is valid action and executes methods from action including logic for injecting to and retrieving data from
     * actions.
     *
     * @param action          object which will be used to realize business logic of single from stream.
     * @param runParameterMap map of parameters used for sharing data between actions.
     *
     * @return true in case when action has been successfully execute otherwise false.
     */
    protected boolean runAction(Object action, ParameterSpool<Object, Object> runParameterMap) {
        boolean retValue = false;
        if (actionData.isValidAction(action)) {
            ActionInfoProxy proxy = actionData.getFrom(action);
            injectParameters(action, proxy, runParameterMap);

            injectContexts(action, proxy, runParameterMap);
            injectConnections(action, proxy, runParameterMap);

            if (executeAction(action)) {
                retrieveRetValues(action, proxy, runParameterMap);
                retValue = true;
            }
        }

        return retValue;
    }

    /**
     * Execute methods from action with following convention for method execution defined by Golem action annotations.
     *
     * @param action object of action which should be used for execution.
     *
     * @return true in case when all method of action are executed successfully, otherwise false.
     */
    protected boolean executeAction(Object action) {
        boolean retValue = true;
        ActionInfoProxy tmp = actionData.getFrom(action);
        for (ActionMethodProxyType type : ActionMethodProxyType.values()) {
            if (!executeActionMethodsGroup(type, tmp.getMethod(type), action)) {
                retValue = false;
                break;
            }
        }
        return retValue;
    }

    /**
     * Executes single action method group with methods order defined by annotations for action methods and sorted by factory which generate
     * Golem action class proxy object. Validates if method is marked critical and in case when result of method is fail it will stop
     * executing of other methods in group.
     *
     * @param type          action type for accessing additional method information used for validation of result of every single method
     *                      execution.
     * @param actionMethods collection of method which are in currently executed group of methods from action.
     * @param action        object of action which will be used for calling method by using Java reflection feature.
     *
     * @return true in case when all methods from current group are executed successfully, otherwise false.
     */
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

                } catch (IllegalAccessException | IllegalArgumentException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    // TODO handling exeption from action by  runner. Disaster recovery scenerions
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, m.getName() + " in " + action.getClass().getSimpleName(), ex.getTargetException());
                }
            }
        }
        return retValue;
    }

    /**
     * Load all action field values which needs to be returned from action into current parameter spool.
     *
     * @param action  object of action which will be used to return action fields value.
     * @param proxy   class information which will be used to identify fields for returning from action object.
     * @param parmMap spool of parameters where values of parameters will be stored or updated.
     */
    protected void retrieveRetValues(Object action, ActionInfoProxy proxy, ParameterSpool<Object, Object> parmMap) {
        List<Field> retValues = proxy.getField(ActionFieldProxyType.ReturnValues);
        if (retValues != null && !retValues.isEmpty()) {
            if (parmMap == null) {
                parmMap = new ParameterSpoolImpl<>();
            }

            for (Field f : retValues) {
                try {
                    parmMap.put(action, f, parmMap);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Setting up all connections required by action from connection spool. Connection id for spool are collected from value of current
     * object parameters.
     *
     * @param action  object where will be injected connections.
     * @param proxy   class information which will be used to identify connection fields in action.
     * @param parmMap spool of parameters used for getting real id of Golem connection depends current data configuration.
     */
    protected void injectConnections(Object action, ActionInfoProxy proxy, ParameterSpool<Object, Object> parmMap) {
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
                            } catch (IllegalArgumentException | IllegalAccessException ex) {
                                Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            Logger.getLogger(Runner.class.getName()).log(Level.FINE, "Field:{0} in action:{1} request type:{2} which is not compatible with type of stored connection:{3}", new Object[]{f.getName(), action.getClass().getName(), type.getName(), connection.getClass().getName()});
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Inserting parameters from parameter spool into action object fields.
     *
     * @param action  target object for inserting parameter values into fields.
     * @param proxy   class information which will be used to identify parameter fields in action.
     * @param parmMap spool of parameters used like source of values for injection.
     */
    protected void injectParameters(Object action, ActionInfoProxy proxy, ParameterSpool<Object, Object> parmMap) {
        Collection<Field> fields = proxy.getField(ActionFieldProxyType.Parameters);
        if ((fields != null) && (!fields.isEmpty()) && (parmMap != null) && (!parmMap.isEmpty())) {
            for (Field f : fields) {
                try {
                    Object parametr = parmMap.get(action, f, parmMap);
                    if (f.getType().isAssignableFrom(parametr.getClass())) {
                        f.set(action, parametr);
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * Inserting context managers and object related to control of execution and other features provided by Golem. Control objects are
     * identified by special annotation provided by Golem implementation.
     *
     * @param action  target object for inserting control objects into fields.
     * @param proxy   class information which will be used to identify control objects fields in action.
     * @param parmMap current action parameters spool for injection in case when action needs to access this data.
     */
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
                    } else if (type.isAssignableFrom(ConnectionFactory.class)) {
                        f.set(action, GolemConnectionFactory.getGlobalFactory());
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
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

}
