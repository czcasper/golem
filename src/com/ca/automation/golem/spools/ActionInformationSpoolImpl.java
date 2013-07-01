/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.interfaces.spools.ActionInformationSpool;
import com.ca.automation.golem.interfaces.spools.keys.ActionInfoKey;
import com.ca.automation.golem.spools.actions.ActionInfoProxyImpl;
import com.ca.automation.golem.spools.actions.SimpleActionStream;
import com.ca.automation.golem.spools.enums.ActionMethodProxyType;
import com.ca.automation.golem.spools.keys.SimpleActionInfoKey;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is storage for
 *
 * @author maslu02
 */
// TODO Documentation: Create JavaDoc on class and public method level.
public class ActionInformationSpoolImpl<A> extends AbstractSpoolImpl<A, ActionInfoKey<Class<?>>, ActionInfoProxy> implements ActionInformationSpool<A> {

    protected Map<ActionMethodProxyType, Comparator<Method>> methodComparator = ActionInfoProxyImpl.createNewComparators();
    protected static ActionInformationSpool<Object> global = new ActionInformationSpoolImpl<>();

    public ActionInformationSpoolImpl() {
    }

    public ActionInformationSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ActionInformationSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    public ActionInformationSpoolImpl(Map<? extends ActionInfoKey<Class<?>>, ? extends ActionInfoProxy> m) {
        super(m);
    }

    public ActionInformationSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public static ActionInformationSpool<Object> getGlobal() {
        return global;
    }

    @Override
    protected ActionInfoKey<Class<?>> createKey(String key) {
        try {
            return new SimpleActionInfoKey(key);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ActionInformationSpoolImpl.class.getName()).log(Level.WARNING, null, ex);
        }
        return null;
    }

    @Override
    protected <P> boolean instancOfKey(P value) {
        return (value instanceof ActionInfoKey);
    }

    @Override
    protected ActionInfoKey<Class<?>> initProxyKey() {
        return new SimpleActionInfoKey((Class<A>) null);
    }

    @Override
    public ActionInformationSpool<A> newInstance() {
        return new ActionInformationSpoolImpl<>();
    }

    @Override
    protected boolean validateFieldType(Field f) throws IllegalAccessException {
        throw new IllegalAccessException("Action information spool doesn't support direct action interaction with action fields");        
    }
    
    @Override
    public boolean isValidAction(Object action) {
        boolean retValue = false;
        if (action != null) {
            Class<?> testClass;
            if (action instanceof ActionInfoKey) {
                action = ((ActionInfoKey) action).get();
            }
            if (action instanceof Class) {
                testClass = (Class<?>) action;

            } else {
                testClass = action.getClass();
            }

            retValue = isValidAction(testClass);
        }
        return retValue;
    }

    @Override
    public boolean isValidAction(Class<?> action) {
        boolean retValue = false;
        if (action != null) {
            searchProxy.set(action);
            if (super.containsKey(searchProxy)) {
                retValue = true;
            } else {
                ActionInfoProxy info = new ActionInfoProxyImpl(methodComparator);
                if (info.loadAction(action, this)) {
                    ActionInfoKey<Class<?>> tmpKey = new SimpleActionInfoKey(action.getClass());
                    super.put(tmpKey, info);
                    retValue = true;
                }
            }
        }
        return retValue;
    }

    @Override
    public <V> ActionStream<A, V> createNewFromObject(List<A> actions) {
        ActionStream<A, V> retValue = null;
        if ((actions != null) && (!actions.isEmpty())) {
            AddressArrayList<A> tmpActions = new AddressArrayList<>(actions.size());
            for (A a : actions) {
                if (isValidAction(a)) {
                    tmpActions.add(a);
                } else {
                    Logger.getLogger(ActionInformationSpool.class.getName()).log(Level.INFO, "Action {0} is not valid runner action.", a.toString());
                }
            }
            if (!tmpActions.isEmpty()) {
                retValue = new SimpleActionStream<>(tmpActions);
            }
        }
        return retValue;
    }

    @Override
    public <V> ActionStream<A, V> createNewFromClasses(List<Class<?>> actions) {
        ActionStream<A, V> retValue = null;
        if ((actions != null) && (!actions.isEmpty())) {
            AddressArrayList<A> tmpActions = new AddressArrayList<>(actions.size());
            for (Class<?> cl : actions) {
                if (isValidAction(cl)) {
                    try {
                        tmpActions.add((A) cl.newInstance());
                    } catch (InstantiationException | IllegalAccessException ex) {
                        Logger.getLogger(ActionInformationSpool.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Logger.getLogger(ActionInformationSpool.class.getName()).log(Level.INFO, "Action class {0} is not valid runner action.", cl.toString());
                }
            }
            if (!tmpActions.isEmpty()) {
                retValue = new SimpleActionStream<>(tmpActions);
            }
        }
        return retValue;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof Class) {
            searchProxy.set((Class<?>) key);
        } else {
            searchProxy.set(key.getClass());
        }
        return super.containsKey(searchProxy);
    }

    @Override
    public ActionInfoProxy get(String key) {
        ActionInfoProxy retValue = null;
        searchProxy.fromString(key);
        if (isValidAction(searchProxy)) {
            retValue = super.get(searchProxy);
        }
        return retValue;
    }

    @Override
    public ActionInfoProxy get(Object key) {
        ActionInfoProxy retValue = null;
        if (isValidAction(key)) {
            Class<?> tmpKey;
            if (key instanceof Class) {
                tmpKey = (Class<?>) key;
            } else {
                tmpKey = key.getClass();
            }
            searchProxy.set(tmpKey);
            retValue = super.get(searchProxy);
        }
        return retValue;
    }
}
