/*
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
 *
 * @author casper
 */
// TODO Documentation: Create JavaDoc on class and public method level.
// TODO Refactoring: Methods with suppressing warning unchecked should be validated and refactored to be more type save (putFrom, getFrom, buildKey)
public abstract class AbstractSpoolImpl<A, K extends AbstractSpoolKey<?>, V> extends LinkedHashMap<K, V> implements AbstractSpool<A, K, V> {

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
    public <P> V put(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException {
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
        if(searchProxy.fromString(key)){
            retValue = this.get(searchProxy);
        }
        return retValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> V get(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException {
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

    protected abstract K createKey(String key);

    protected abstract <P> boolean instancOfKey(P value);

    protected abstract K initProxyKey();

    protected abstract boolean validateFieldType(Field f) throws IllegalAccessException;
}
