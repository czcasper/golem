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

import cz.a_d.automation.golem.context.RunActionStackImpl;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.interfaces.context.RunActionStack;
import cz.a_d.automation.golem.interfaces.context.managers.RunActionStackManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of interface specific to run action stack manager.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 * @param <C> the type of object used for validation of run results
 */
public class RunActionStackManagerImpl<T, C, V> extends AbstractContextManager<T, C, RunActionStack<T>, V> implements RunActionStackManager<T, V> {

    /**
     *  Creating instance of manager of run action stack.
     *
     * @param context run context for which is this manager implementation registered. Must be different from null.
     */
    public RunActionStackManagerImpl(RunContextImpl<T, C, V> context) {
        super(context);
    }

    @Override
    public List<RunActionStack<T>> getActive() {
        return currentList;
    }

    @Override
    protected void beforeNextInList() {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void afterNextInList() {
        try {
            current = (RunActionStack<T>) current.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RunActionStackManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected boolean currentFinilizer() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadManger(T action) {
        if (current == null) {
            currentList = managed.get(action);
            index = 0;
            try {

                current = (RunActionStack<T>) currentList.get(index++).clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RunActionStackManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RunActionStack<T> setupManager(T action, Object... params) {
        RunActionStack<T> retValue = null;
        if ((action != null) && (params != null) && (params.length > 0)) {
            RunActionStack<T> tmp = new RunActionStackImpl<>();
            if (tmp.setupStack(action, (T[]) params)) {
                retValue = tmp;
            }
        }
        return retValue;
    }
}
