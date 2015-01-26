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
 * Implementation of spool of cached action proxy information collected from annotations defined in action class.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 */
public class ActionInformationSpoolImpl<A> extends AbstractSpoolImpl<A, ActionInfoKey<Class<?>>, ActionInfoProxy> implements ActionInformationSpool<A> {

    private static final long serialVersionUID = 1L;
    /**
     * Instance of comparator used to sort methods in proxy objects.
     */
    protected Map<ActionMethodProxyType, Comparator<Method>> methodComparator = ActionInfoProxyImpl.createNewComparators();

    /**
     * Global instance of spool used to share information mapped from actions across all runner thread.
     */
    protected final static ActionInformationSpool<Object> global = new ActionInformationSpoolImpl<>();

    /**
     * Constructs an empty <tt>Action information spool</tt> with capacity 16, load factor 0.75 and sorting values by based on amount of
     * access to value.
     */
    public ActionInformationSpoolImpl() {
        super();
    }

    /**
     * Constructs an empty <tt>Action information spool</tt> with the specified initial capacity and load factor and sorting values by based
     * on amount of access to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     */
    public ActionInformationSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructs an empty <tt>Action information spool</tt> with the specified initial capacity and sorting values by based on amount of
     * access to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     */
    public ActionInformationSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a new <tt>Action information spool</tt> with the same mappings as the specified <tt>Map</tt>. The <tt>Spool</tt> is
     * created with default load factor (0.75) and an initial capacity sufficient to hold the mappings in the specified <tt>Map</tt>.
     *
     * @param m the map whose mappings are to be placed in this spool.
     */
    public ActionInformationSpoolImpl(Map<? extends ActionInfoKey<Class<?>>, ? extends ActionInfoProxy> m) {
        super(m);
    }

    /**
     * Constructs an empty <tt>Action information spool</tt> instance with the specified initial capacity, load factor and ordering mode.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     * @param accessOrder     the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
     */
    public ActionInformationSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    /**
     * Getter for global instance of Action information spool.
     *
     * @return global instance of action information spool.
     */
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

    /**
     * Construct new action information spool key from given object instance.
     *
     * @param key instance of object which will be used to create new key instance. Can be valid action instance, class instance, string or
     *            instance of Action information key.
     * @return instance of key if input parameter is valid, otherwise null.
     */
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
