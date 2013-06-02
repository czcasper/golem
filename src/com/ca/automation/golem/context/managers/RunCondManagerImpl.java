/*
 */
package com.ca.automation.golem.context.managers;

import com.ca.automation.golem.context.RunConditionImpl;
import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.interfaces.context.RunCondition;
import com.ca.automation.golem.interfaces.context.managers.RunCondManager;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
public class RunCondManagerImpl<T, C extends Object, V> extends AbstractContextManager<T, C, RunCondition<T, C>, V> implements RunCondManager<T, C, V> {

    public RunCondManagerImpl(RunContextImpl<T, C, V> context) {
        super(context);
    }

    @Override
    public boolean setup(T action, C expect, long counter) {
        Object parms[] = {expect, counter};
        return setup(action, parms);
    }

    @Override
    protected void beforeNextInList() {
        current = null;
        index--;
    }

    @Override
    protected void afterNextInList() {
        currentList.remove(index--);
    }

    @Override
    protected boolean currentFinilizer() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadManger(T action) {
        if (currentList == null) {
            currentList = new LinkedList<RunCondition<T, C>>();
        }
        List<RunCondition<T, C>> found = managed.get(action);
        if (!found.isEmpty()) {
            for (RunCondition<T, C> c : found) {

                try {

                    currentList.add((RunCondition<T, C>) c.clone());
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(RunCondManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (!currentList.isEmpty()) {
                index = currentList.size() - 1;
                current = currentList.get(index);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RunCondition<T, C> setupManager(T action, Object... params) {
        RunCondition<T, C> retValue = null;
        if ((context != null) && (action != null) && (params.length == 2)
                && (params[0] instanceof Object) && (params[1] instanceof Long)) {
            RunConditionImpl<T, C> cond = new RunConditionImpl<T, C>();
            C expect = (C) params[0];
            long counter = (Long) params[1];
            if (cond.setupCond(action, expect, counter)) {
                retValue = cond;
            }
        }
        return retValue;
    }

    @Override
    public RunCondition<T, C> getCurrent() {
        return super.getCurrent();
    }

    @Override
    public List<RunCondition<T, C>> getActive() {
        return currentList;
    }
}
