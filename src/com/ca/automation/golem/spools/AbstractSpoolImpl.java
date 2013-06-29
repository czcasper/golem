/*
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.ParameterSpool;
import com.ca.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import com.ca.automation.golem.spools.enums.ActionFieldProxyType;
import com.ca.automation.golem.spools.keys.SimpleParameterKey;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
// TODO Documentation: Create JavaDoc on class and public method level.
// TODO Refactoring: Methods with suppressing warning unchecked should be validated and refactored to be more type save (put, get, buildKey)
public abstract class AbstractSpoolImpl<A, K extends AbstractSpoolKey<?>, V> extends LinkedHashMap<K, V> implements AbstractSpool<A, K, V>, Cloneable {

    protected K searchProxy = null;

    public AbstractSpoolImpl() {
        this(16, 0.75f, true);
    }

    public AbstractSpoolImpl(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, true);
    }

    public AbstractSpoolImpl(int initialCapacity) {
        this(initialCapacity, 0.75f, true);
    }

    public AbstractSpoolImpl(Map<? extends K, ? extends V> m) {
        super(m);
        initProxy();
    }

    public AbstractSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        initProxy();
    }

    @Override
    public V put(String key, V value) {
        V retValue = null;
        if ((key != null) && (!key.isEmpty())) {
            retValue = this.put(createKey(key), value);
        }
        return retValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> V put(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException {
        V retValue = null;
        K tmpKey = buildKey(action, f, parameters, true);
        if (tmpKey != null) {
            V value = (V) f.get(action);
            retValue = put(tmpKey, value);
        }
        return retValue;
    }

    @Override
    public boolean contains(String key) {
        searchProxy.fromString(key);
        return this.containsKey(searchProxy);
    }

    @Override
    public V get(String key) {
        searchProxy.fromString(key);
        return this.get(searchProxy);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> V get(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException {
        V retValue = null;
        K tmpKey = buildKey(action, f, parameters, false);
        if ((tmpKey != null) && (containsKey(tmpKey))) {
            retValue = (V) f.get(action);
            f.set(action, get(tmpKey));
        }
        return retValue;
    }

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

    protected final void initProxy() {
        searchProxy = initProxyKey();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        LinkedHashMap<K, V> retValue = (LinkedHashMap<K, V>) super.clone();
        for (Entry<K, V> e : retValue.entrySet()) {
            if (e.getValue() instanceof Cloneable) {
                V cl = e.getValue();
                try {
                    Method clone = cl.getClass().getDeclaredMethod("clone");
                    clone.setAccessible(true);
                    retValue.put(e.getKey(), (V) clone.invoke(cl));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(AbstractSpoolImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return retValue;
    }

    protected abstract K createKey(String key);

    protected abstract <P> boolean instancOfKey(P value);

    protected abstract K initProxyKey();
}
