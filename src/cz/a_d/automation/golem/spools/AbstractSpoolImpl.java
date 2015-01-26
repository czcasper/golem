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

import cz.a_d.automation.golem.interfaces.spools.AbstractSpool;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleParameterKey;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of methods commonly required from Golem spools. Class providing implementation of all method required by interface and
 * require to implement protected method specific to objects stored in spool.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 * @param <K> the type of key value in spool.
 * @param <V> the type of value in spool.
 */
public abstract class AbstractSpoolImpl<A, K extends AbstractSpoolKey<?>, V> extends LinkedHashMap<K, V> implements AbstractSpool<A, K, V> {

    /**
     * Instance of key object used for searching to prevent creation of new key instance during every search operation.
     */
    protected K searchProxy = null;

    /**
     * Constructs an empty <tt>Spool</tt> with capacity 16, load factor 0.75 and sorting values by based on amount of access to value.
     */
    public AbstractSpoolImpl() {
        this(16, 0.75f, true);
    }

    /**
     * Constructs an empty <tt>Spool</tt> with the specified initial capacity and load factor and sorting values by based on amount of
     * access to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     */
    public AbstractSpoolImpl(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, true);
    }

    /**
     * Constructs an empty <tt>Spool</tt> with the specified initial capacity and sorting values by based on amount of access to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     */
    public AbstractSpoolImpl(int initialCapacity) {
        this(initialCapacity, 0.75f, true);
    }

    /**
     * Constructs a new <tt>Spool</tt> with the same mappings as the specified <tt>Map</tt>. The <tt>Spool</tt> is created with default load
     * factor (0.75) and an initial capacity sufficient to hold the mappings in the specified <tt>Map</tt>.
     *
     * @param m the map whose mappings are to be placed in this spool.
     */
    public AbstractSpoolImpl(Map<? extends K, ? extends V> m) {
        super(m);
        initProxy();
    }

    /**
     * Constructs an empty <tt>Spool</tt> instance with the specified initial capacity, load factor and ordering mode.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     * @param accessOrder     the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
     */
    public AbstractSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        initProxy();
    }

    @Override
    public V putFrom(String key, V value) {
        V retValue = null;
        if ((key != null) && (!key.isEmpty())) {
            K tmpKey = createKey(key);
            if (tmpKey != null) {
                retValue = this.put(tmpKey, value);
            }
        }
        return retValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> V put(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalAccessException {
        V retValue = null;
        if (validateFieldType(f)) {
            K tmpKey = buildKey(action, f, parameters, true);
            if (tmpKey != null) {
                V value = (V) f.get(action);
                retValue = put(tmpKey, value);
            }
        }
        return retValue;
    }

    @Override
    public boolean containsFrom(String key) {
        boolean retValue = false;
        if (searchProxy.fromString(key)) {
            retValue = this.containsKey(searchProxy);
        }
        return retValue;
    }

    @Override
    public V getFrom(String key) {
        V retValue = null;
        if (searchProxy.fromString(key)) {
            retValue = this.get(searchProxy);
        }
        return retValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> V get(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalAccessException {
        V retValue = null;
        if (validateFieldType(f)) {
            K tmpKey = buildKey(action, f, parameters, false);
            if ((tmpKey != null) && (containsKey(tmpKey))) {
                retValue = (V) f.get(action);
                f.set(action, get(tmpKey));
            }
        }
        return retValue;
    }

    /**
     * Construct spool key from given parameters.
     *
     * @param <P>           the type of value used in parameter spool.
     * @param action        instance of action used to generate key value. Must be different from null.
     * @param f             field from action class which will be stored under generated key.
     * @param parameters    spool of parameters used for providing feature of pointer value implemented by Golem parameters.
     * @param createNewFlag flag for creating new instance if value of flag is true, otherwise proxy object is filled by data collected from
     *                      action.
     * @return instance of key if all required parameters are valid, otherwise false.
     */
    @SuppressWarnings("unchecked")
    protected <P> K buildKey(A action, Field f, ParameterSpool<A, P> parameters, boolean createNewFlag) {
        K retValue = null;
        if ((action != null) && (f != null)) {
            ActionFieldProxyType type = ActionFieldProxyType.getType(f);
            if (type != null) {
                String pointer = type.getPointer(f);
                if ((pointer != null) && (!pointer.isEmpty())) {
                    SimpleParameterKey tmpKey = new SimpleParameterKey(pointer);
                    if (parameters.containsKey(tmpKey)) {
                        P tmp = parameters.get(tmpKey);
                        if (tmp instanceof String) {
                            if (createNewFlag) {
                                retValue = createKey((String) tmp);
                            } else {
                                searchProxy.fromString((String) tmp);
                                retValue = searchProxy;
                            }

                        } else if (instancOfKey(tmp)) {
                            retValue = (K) tmp;
                        } else if (tmp == null) {
                            Logger.getLogger(this.getClass().getName()).log(Level.FINEST, "Pointer is pointing to null value. Null is not acceptable key");
                        } else {
                            Logger.getLogger(ConnectionSpoolImpl.class.getName()).log(Level.FINEST, "Pointer is pointing to unsuported type:{0}. Connection key can be String or class with implemented ConnectionKey interface.", tmp.getClass().getName());
                        }
                    } else {
                        Logger.getLogger(ConnectionSpoolImpl.class.getName()).log(Level.FINEST, "Non existing pointer value:{0} used in class:{1} on field:{2}", new Object[]{pointer, action.getClass().getName(), f.getName()});
                    }
                }

                if (retValue == null) {
                    String name = type.getName(f);
                    if (name != null) {
                        if (name.isEmpty()) {
                            name = action.getClass().getName() + "." + f.getName();
                        }
                        if (createNewFlag) {
                            retValue = createKey(name);
                        } else {
                            searchProxy.fromString(name);
                            retValue = searchProxy;
                        }

                    }
                }

            }
        }
        return retValue;
    }

    /**
     * Method is just hiding calling of abstract method from constructor.
     */
    protected final void initProxy() {
        searchProxy = initProxyKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        AbstractSpool<A, K, V> retValue = (AbstractSpool<A, K, V>) super.clone();
        for (Entry<K, V> e : retValue.entrySet()) {
            if (e.getValue() instanceof Cloneable) {
                V cl = e.getValue();
                try {
                    final Method clone = cl.getClass().getDeclaredMethod("clone");
                    AccessController.doPrivileged(new PrivilegedAction<Object>() {
                        @Override
                        public Object run() {
                            clone.setAccessible(true);
                            return null;
                        }
                    });
                    retValue.put(e.getKey(), (V) clone.invoke(cl));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(AbstractSpoolImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return retValue;
    }

    /**
     * Creating instance of new key object used by spool from string representation.
     *
     * @param key string value of key object used by spool for indexing.
     *
     * @return instance of key in case when given parameter is valid representation of key implementation, otherwise null.
     */
    protected abstract K createKey(String key);

    /**
     * Testing if given parameter instance is instance of key used by spool for indexing.
     *
     * @param <P>   the type of tested object.
     * @param value instance of object which will be validated for type.
     * @return true in case when given parameter is instance of class used by spool for key object, otherwise false.
     */
    protected abstract <P> boolean instancOfKey(P value);

    /**
     * Creating new instance of Spool key object, this instance is used like proxy key for searching operations.
     *
     * @return new empty instance of key object implementation.
     */
    protected abstract K initProxyKey();

    /**
     * Testing if field has annotations which determine type of field supported by current spool implementation. This method is used in
     * support for direct interaction between actions and spool.
     *
     * @param f instance of field object used for collecting information about type of field defined by annotation.
     * @return true in case when field is valid object managed by spool, otherwise false.
     *
     * @throws IllegalAccessException in case when something goes wrong with validation.
     */
    protected abstract boolean validateFieldType(Field f) throws IllegalAccessException;
}
