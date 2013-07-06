/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.spools.ActionInformationSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import cz.a_d.automation.golem.spools.actions.ActionInfoProxyImpl;
import cz.a_d.automation.golem.spools.actions.SimpleActionStream;
import cz.a_d.automation.golem.spools.enums.ActionMethodProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleActionInfoKey;
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
                    ActionInfoKey<Class<?>> tmpKey = new SimpleActionInfoKey(action);
                    super.put(tmpKey, info);
                    retValue = true;
                }
            }
        }
        return retValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    // TODO Refactoring: validate if the is chance to provide same functionality with more type save implementation.
    public <V> ActionStream<A, V> createNewFromObject(List<A> actions) {
        ActionStream<A, V> retValue = null;
        if ((actions != null) && (!actions.isEmpty())) {
            AddressArrayList<A> tmpActions = new AddressArrayList<>(actions.size());
            for (A a : actions) {
                if (isValidAction(a)) {
                    if (a instanceof Class) {
                        Class<?> aClass = (Class<?>) a;
                        try {
                            tmpActions.add((A) aClass.newInstance());
                        } catch (InstantiationException | IllegalAccessException ex) {
                            Logger.getLogger(ActionInformationSpoolImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        tmpActions.add(a);
                    }
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
    @SuppressWarnings("unchecked")
    // TODO Refactoring: try to find type save way how to provide same functionality.
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
    public boolean containsFrom(Object key) {
        boolean retValue = false;
        ActionInfoKey<Class<?>> tmpKey = createKeyFrom(key);
        if (tmpKey != null) {
            retValue = super.containsKey(tmpKey);
        }
        return retValue;
    }

    @Override
    public ActionInfoProxy put(ActionInfoKey<Class<?>> key, ActionInfoProxy value) {
        ActionInfoProxy retValue = null;
        if ((key != null) && (value != null) && (value.isValid())) {
            retValue = super.put(key, value);
        }
        return retValue;
    }

    @Override
    public ActionInfoProxy putFrom(String key, ActionInfoProxy value) {
        ActionInfoProxy retValue = null;
        if ((value != null) && (value.isValid())) {
            retValue = super.putFrom(key, value);
        }
        return retValue;
    }

    @Override
    public ActionInfoProxy getFrom(String key) {
        ActionInfoProxy retValue = null;
        ActionInfoKey<Class<?>> tmpKey = createKeyFrom(key);
        if ((tmpKey != null) && (isValidAction(tmpKey.get()))) {
            retValue = super.get(tmpKey);
        }
        return retValue;
    }

    @Override
    public ActionInfoProxy getFrom(Object key) {
        ActionInfoProxy retValue = null;
        if (isValidAction(key)) {
            retValue = super.get(createKeyFrom(key));
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    protected ActionInfoKey<Class<?>> createKeyFrom(Object key) {
        ActionInfoKey<Class<?>> retValue = null;
        if (key != null) {
            if (key instanceof String) {
                String tmp = (String) key;
                if (searchProxy.fromString(tmp)) {
                    retValue = searchProxy;
                }
            } else if (key instanceof Class) {
                Class tmp = (Class) key;
                if (searchProxy.set(tmp)) {
                    retValue = searchProxy;
                }
            } else if (key instanceof ActionInfoKey) {
                // TODO Refactoring: try to find type save way how to provide same functionality.
                retValue = (ActionInfoKey<Class<?>>) key;
            } else {
                Class<?> tmp = key.getClass();
                if (searchProxy.set(tmp)) {
                    retValue = searchProxy;
                }
            }
        }
        return retValue;
    }
}
