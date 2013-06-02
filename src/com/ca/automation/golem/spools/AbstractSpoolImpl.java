/*
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.annotations.fields.RunConnection;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.AbstractSpoolKey;
import com.ca.automation.golem.interfaces.spools.ParameterKey;
import com.ca.automation.golem.spools.keys.SimpleParameterKey;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
public abstract class AbstractSpoolImpl<A, K extends AbstractSpoolKey, V> extends LinkedHashMap<K, V> implements AbstractSpool<A, K, V>, Cloneable {

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
        return this.put(createKey(key), value);
    }

    @Override
    public <P> V put(A action, Field f, AbstractSpool<A, ParameterKey, P> parameters, V value) {
        V retValue = null;
        K tmpKey = buildKey(action, f, parameters, true);
        if (tmpKey != null) {
            retValue = put(tmpKey, value);
        }
        return retValue;
    }

    @Override
    public boolean contains(String key) {
        searchProxy.load(key);
        return this.containsKey(searchProxy);
    }

    @Override
    public V get(String key) {
        searchProxy.load(key);
        return this.get(searchProxy);
    }

    @Override
    public <P> V get(A action, Field f, AbstractSpool<A, ParameterKey, P> parameters) {
        V retValue = null;
        K tmpKey = buildKey(action, f, parameters, false);
        if ((tmpKey != null) && (containsKey(tmpKey))) {
            retValue = get(tmpKey);
        }
        return retValue;
    }

    protected <P> K buildKey(A action, Field f, Map<ParameterKey, P> parameters, boolean createNewFlag) {
        K retValue = null;
        if ((f != null) && (action != null)) {
            RunConnection fieldAnnotation = f.getAnnotation(RunConnection.class);
            if (parameters != null) {
                String pointerKey = fieldAnnotation.pointer();
                if (pointerKey != null && !pointerKey.isEmpty()) {
                    SimpleParameterKey tmpKey = new SimpleParameterKey(pointerKey);
                    if (parameters.containsKey(tmpKey)) {
                        P tmp = parameters.get(tmpKey);
                        if (tmp instanceof String) {
                            if (createNewFlag) {
                                retValue = createKey((String) tmp);
                            } else {
                                searchProxy.load((String) tmp);
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
                        Logger.getLogger(ConnectionSpoolImpl.class.getName()).log(Level.FINEST, "Non existing pointer value:{0} used in class:{1} on field:{2}", new Object[]{pointerKey, action.getClass().getName(), f.getName()});
                    }
                }
            }

            if (retValue == null) {
                String name = fieldAnnotation.name();
                if (name == null || name.isEmpty()) {
                    name = action.getClass().getName() + f.getName();
                }

                if (createNewFlag) {
                    retValue = createKey(name);
                } else {
                    searchProxy.load(name);
                    retValue = searchProxy;
                }
            }
        }
        return retValue;
    }

    protected final void initProxy() {
        searchProxy = initProxyKey();
    }

    @Override
    public Object clone() {
        return super.clone();
    }

    protected abstract K createKey(String key);

    protected abstract <P> boolean instancOfKey(P value);

    protected abstract K initProxyKey();
}
