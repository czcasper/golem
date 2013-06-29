/*
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.ActionStreamSpool;
import com.ca.automation.golem.interfaces.spools.keys.ActionStreamKey;
import com.ca.automation.golem.spools.keys.SimpleActionStreamKey;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
public class ActionStreamSpoolImpl<A, V> extends AbstractSpoolImpl<A, ActionStreamKey<?>, ActionStream<A, V>> implements ActionStreamSpool<A, V> {

    protected static ActionStreamSpool<Object,Object> global = new ActionStreamSpoolImpl<Object, Object>();
    

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

    public static ActionStreamSpool<Object,Object> getGlobal(){
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
    protected ActionStreamKey initProxyKey() {
        return new SimpleActionStreamKey(null);
    }

    @Override
    public ActionStreamSpool<A,V> newInstance() {
        return new ActionStreamSpoolImpl<A, V>();
    }

    @SuppressWarnings("unchecked")
    public ActionStream<A, V> get(ActionStreamKey key) {
        ActionStream<A, V> retValue = null;
        if (containsKey(key)) {
            try {
                retValue = (ActionStream<A, V>) super.get(key).clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(ActionStreamSpoolImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retValue;
    }
}
