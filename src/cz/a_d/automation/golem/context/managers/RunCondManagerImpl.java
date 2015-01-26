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
package cz.a_d.automation.golem.context.managers;

import cz.a_d.automation.golem.context.RunConditionImpl;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.interfaces.context.RunCondition;
import cz.a_d.automation.golem.interfaces.context.managers.RunCondManager;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of interface specific to manager of run action result validation.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 * @param <C> the type of object used for validation of run results
 */
public class RunCondManagerImpl<T, C extends Object, V> extends AbstractContextManager<T, C, RunCondition<T, C>, V> implements RunCondManager<T, C, V> {

    /**
     * Creating instance of manager of run action result validation.
     *
     * @param context run context for which is this manager implementation registered. Must be different from null.
     */
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
            currentList = new LinkedList<>();
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
            RunConditionImpl<T, C> cond = new RunConditionImpl<>();
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
