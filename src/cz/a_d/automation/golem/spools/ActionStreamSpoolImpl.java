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
 *
 * @author maslu02
 */
// TODO Documetation: create JavaDoc minimally on public method and class level.
public class ActionStreamSpoolImpl<A, V> extends AbstractSpoolImpl<A, ActionStreamKey<?>, ActionStream<A, V>> implements ActionStreamSpool<A, V> {

    protected static ActionStreamSpool<Object, Object> global = new ActionStreamSpoolImpl<>();

    public ActionStreamSpoolImpl() {
        super();
    }

    public ActionStreamSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ActionStreamSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    public ActionStreamSpoolImpl(Map<? extends ActionStreamKey<?>, ? extends ActionStream<A, V>> m) {
        super(m);
    }

    public ActionStreamSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

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
