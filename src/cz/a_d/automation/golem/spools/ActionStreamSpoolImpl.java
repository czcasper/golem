/*
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.ActionStreamSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;
import cz.a_d.automation.golem.spools.keys.SimpleActionStreamKey;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of spool of action streams loaded and prepared to be interpreted by Golem runner.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 * @param <V> the type of value in spool.
 */
public class ActionStreamSpoolImpl<A, V> extends AbstractSpoolImpl<A, ActionStreamKey<?>, ActionStream<A, V>> implements ActionStreamSpool<A, V> {

    private static final long serialVersionUID = 1L;
    /**
     * Global instance of spool used to share loaded action streams across all runner thread.
     */
    protected final static ActionStreamSpool<Object, Object> global = new ActionStreamSpoolImpl<>();

    /**
     * Constructs an empty <tt>Action stream spool</tt> with capacity 16, load factor 0.75 and sorting values by based on amount of access
     * to value.
     */
    public ActionStreamSpoolImpl() {
        super();
    }

    /**
     * Constructs an empty <tt>Action stream spool</tt> with the specified initial capacity and load factor and sorting values by based on
     * amount of access to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     */
    public ActionStreamSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructs an empty <tt>Action stream spool</tt> with the specified initial capacity and sorting values by based on amount of access
     * to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     */
    public ActionStreamSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a new <tt>Action stream spool</tt> with the same mappings as the specified <tt>Map</tt>. The <tt>Spool</tt> is created
     * with default load factor (0.75) and an initial capacity sufficient to hold the mappings in the specified <tt>Map</tt>.
     *
     * @param m the map whose mappings are to be placed in this spool.
     */
    public ActionStreamSpoolImpl(Map<? extends ActionStreamKey<?>, ? extends ActionStream<A, V>> m) {
        super(m);
    }

    /**
     * Constructs an empty <tt>Action stream spool</tt> instance with the specified initial capacity, load factor and ordering mode.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     * @param accessOrder     the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
     */
    public ActionStreamSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    /**
     * Getter for global instance of Action stream spool.
     *
     * @return global instance of action stream spool.
     */
    public static ActionStreamSpool<Object, Object> getGlobal() {
        return global;
    }

    @Override
    protected ActionStreamKey createKey(String key) {
        return new SimpleActionStreamKey(key);
    }

    @Override
    protected <P> boolean instancOfKey(P value) {
        return (value instanceof ActionStreamKey);
    }

    @Override
    protected boolean validateFieldType(Field f) throws IllegalAccessException {
        throw new IllegalAccessException("Action stream spool doesn't support direct action interaction with action fields");
    }

    @Override
    protected ActionStreamKey initProxyKey() {
        return new SimpleActionStreamKey(null);
    }

    @Override
    public ActionStreamSpool<A, V> newInstance() {
        return new ActionStreamSpoolImpl<>();
    }

    @Override
    public ActionStream<A, V> put(ActionStreamKey<?> key, ActionStream<A, V> value) {
        ActionStream<A, V> retValue = null;
        if ((key != null) && (value != null)) {
            List<A> actionList = value.getActionList();
            if ((actionList != null) && (!actionList.isEmpty())) {
                retValue = super.put(key, value);
            }
        }
        return retValue;
    }

    @Override
    public ActionStream<A, V> get(Object key) {
        ActionStream<A, V> retValue = super.get(key);
        return narrowInstance(retValue);
    }

    @Override
    public ActionStream<A, V> putFrom(String key, ActionStream<A, V> value) {
        ActionStream<A, V> retValue = null;
        if (value != null) {
            List<A> actionList = value.getActionList();
            if ((actionList != null) && (!actionList.isEmpty())) {
                retValue = super.putFrom(key, value);
            }
        }

        return retValue;
    }

    @Override
    public ActionStream<A, V> getFrom(String key) {
        ActionStream<A, V> retValue = super.getFrom(key);
        return narrowInstance(retValue);
    }

    /**
     * Cloning instance of action stream stored in spool, to provide new fresh instance in state which has been put into stream to every
     * caller.
     *
     * @param stream instance of stream stored in map.
     * @return cloned instance of stream accepted in parameter or null if something go wrong.
     */
    @SuppressWarnings("unchecked")
    protected ActionStream<A, V> narrowInstance(ActionStream<A, V> stream) {
        ActionStream<A, V> retValue = null;
        if (stream != null) {
            try {
                retValue = (ActionStream<A, V>) stream.clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ActionStreamSpoolImpl.class.getName()).log(Level.SEVERE, null, ex);
                retValue = null;
            }
        }
        return retValue;
    }
}
