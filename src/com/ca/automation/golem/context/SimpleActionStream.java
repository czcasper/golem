/*
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.interfaces.ActionStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public class SimpleActionStream<T,K,V> implements ActionStream<T,K,V> {
    
    protected Map<K,V> parmSpool;
    protected List<T> actions;
    protected ResetableIterator<T> it;

    public SimpleActionStream(List<T> actions) {
        if (actions == null) {
            throw new NullPointerException("Action stream cannot be initializet by null array of actions");
        }
        this.actions = actions;
        it = new ResetableIterator<T>(this.actions.iterator());
    }

    @Override
    public List<T> getActionList() {
        return actions;
    }

    @Override
    public void setParameter(Map<K, V> actionParams) {
        this.parmSpool = actionParams;
    }

    @Override
    public Map<K, V> getParameterMap() {
        return parmSpool;
    }

    @Override
    public ResetableIterator<T> resetableIterator() {
        return it;
    }
}
