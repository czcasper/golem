/*
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.spools.ConnectionSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleConnectionKey;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Implementation of spool of action connections used by actions from action stream processed by Golem runner.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 */
public class ConnectionSpoolImpl<A> extends AbstractSpoolImpl<A, ConnectionKey<?>, Connection> implements ConnectionSpool<A> {

    private static final long serialVersionUID = 1L;
    /**
     * Global instance of spool used to share connections across all runner thread.
     */
    protected final static ConnectionSpool<Object> global = new ConnectionSpoolImpl<>();

    /**
     * Constructs an empty <tt>Connection spool</tt> with capacity 16, load factor 0.75 and sorting values by based on amount of access to
     * value.
     */
    public ConnectionSpoolImpl() {
        super();
    }

    /**
     * Constructs an empty <tt>Connection spool</tt> with the specified initial capacity and load factor and sorting values by based on
     * amount of access to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     */
    public ConnectionSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructs an empty <tt>Connection spool</tt> with the specified initial capacity and sorting values by based on amount of access to
     * value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     */
    public ConnectionSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a new <tt>Connection spool</tt> with the same mappings as the specified <tt>Map</tt>. The <tt>Spool</tt> is created with
     * default load factor (0.75) and an initial capacity sufficient to hold the mappings in the specified <tt>Map</tt>.
     *
     * @param m the map whose mappings are to be placed in this spool.
     */
    public ConnectionSpoolImpl(Map<? extends ConnectionKey<?>, ? extends Connection> m) {
        super(m);
    }

    /**
     * Constructs an empty <tt>Connection spool</tt> instance with the specified initial capacity, load factor and ordering mode.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     * @param accessOrder     the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
     */
    public ConnectionSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    /**
     * Getter for global instance of Connection spool.
     *
     * @return global instance of connection spool.
     */
    public static ConnectionSpool<Object> getGlobal() {
        return global;
    }

    @Override
    public ConnectionSpool<A> newInstance() {
        return new ConnectionSpoolImpl<>();
    }

    @Override
    protected ConnectionKey createKey(String key) {
        return new SimpleConnectionKey(key);
    }

    @Override
    protected <P> boolean instancOfKey(P value) {
        return (value instanceof ConnectionKey);
    }

    @Override
    protected ConnectionKey initProxyKey() {
        return new SimpleConnectionKey(null);
    }

    @Override
    protected boolean validateFieldType(Field f) throws IllegalAccessException {
        boolean retValue = true;
        if (f != null) {
            ActionFieldProxyType type = ActionFieldProxyType.getType(f);
            if ((type != null) && (type == ActionFieldProxyType.Connections)) {
                retValue = true;
            } else {
                String usedType = "null";
                if (type != null) {
                    usedType = type.toString();
                }
                throw new IllegalAccessException("Parameter spool doesn''t supports field annotated by type:" + usedType);
            }
        }
        return retValue;
    }
}
