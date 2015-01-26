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
package cz.a_d.automation.golem.context;

import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.context.managers.RunActionStackManagerImpl;
import cz.a_d.automation.golem.context.managers.RunCondManagerImpl;
import cz.a_d.automation.golem.context.managers.RunCycleManagerImpl;
import cz.a_d.automation.golem.context.managers.RunDelayIntervalManagerImpl;
import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.context.RunCondition;
import cz.a_d.automation.golem.interfaces.context.managers.RunActionStackManager;
import cz.a_d.automation.golem.interfaces.context.managers.RunCondManager;
import cz.a_d.automation.golem.interfaces.context.managers.RunContextManagers;
import cz.a_d.automation.golem.interfaces.context.managers.RunCycleManager;
import cz.a_d.automation.golem.interfaces.context.managers.RunDelayIntervalManager;
import java.util.Iterator;

/**
 * Implements interaction between particular Golem features. Single feature is implemented by manager of feature. Every feature is separated
 * from each other and loaded on demand of actions. Class is also keeping information about Action stream which is currently executed to
 * provide option for managers to change in flow of action stream.
 *
 * @author casper
 * @param <T> the type of actions managed by this context
 * @param <C> the type of value used for result validation.
 * @param <V> the type of value used in parameter spool.
 */
public class RunContextImpl<T, C, V> implements RunContextManagers<T, C, V>, Iterator<T> {

    /**
     * Pointer to currently processed action stream. Stream is shared between managers and used to provide converted flow of actions based
     * on features provided by manager.
     */
    protected ActionStream<T, V> currentStream;

    /**
     * Iterator used to provide next action from stream modified by Golem features. Feature managers are using this iterator to change
     * context and add or remove actions from stream.
     */
    protected ResetableIterator<T> it;

    /**
     * Instance of manager for cycles feature in Golem streams used in this context. This manager allows repeating actions in cycles.
     */
    protected RunCycleManager<T, V> cycle;

    /**
     * Instance of manager for waiting feature in Golem streams used in this context. This manager allows wait defined time frame after
     * action method.
     */
    protected RunDelayIntervalManager<T, V> timer;

    /**
     * Instance of manager for action stack feature in Golem streams used in this context. This manager allows add action in stack and
     * execute after specified action.
     */
    protected RunActionStackManager<T, V> stack;

    /**
     * Instance of manager for testing result from action method feature in Golem streams used in this context. This manager allows to
     * validate results returned by action methods and stop execution of action stream.
     */
    protected RunCondManager<T, C, V> conds;

    @Override
    public void setActionStream(ActionStream<T, V> stream) {
        if (stream == null) {
            throw new NullPointerException("RunContext cannot be intilized by null ActionStream");
        }
        this.currentStream = stream;
        it = currentStream.resetableIterator();
    }

    @Override
    public ActionStream<T, V> getActionStream() {
        return currentStream;
    }

    @Override
    public ResetableIterator<T> resetableIterator() {
        ResetableIterator<T> retValue = null;
        if (currentStream != null) {
            retValue = currentStream.resetableIterator();
        }
        return retValue;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        boolean retValue = false;
        if (it != null) {
            if (((stack != null) && (stack.hasNext()))
                    || ((cycle != null) && (cycle.hasNext()))
                    || it.hasNext()) {
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public T next() {
        if (it == null) {
            throw new IllegalStateException("Run Context is not initialize by run steps");
        }

        T retValue = null;
        boolean processLoad = true;
        if ((stack != null) && (stack.hasNext())) {
            retValue = stack.next();
            processLoad = false;
        } else if ((cycle != null) && (cycle.hasNext())) {
            retValue = cycle.next();
        } else if (it.hasNext()) {
            retValue = it.next();
        } else {
            processLoad = false;
        }

        if (processLoad) {
            if (stack != null) {
                stack.load(retValue);
            }
            if (cycle != null) {
                cycle.load(retValue);
            }
            if (timer != null) {
                timer.load(retValue);
            }
        }

        if (timer != null) {
            timer.next();
        }
        return retValue;
    }

    @Override
    public void remove() {
        if (it == null) {
            throw new IllegalStateException("Data array wasn't initialized.");
        }
        it.remove();
    }

    @Override
    public RunDelayIntervalManager<T, V> getDelayManager() {
        return timer;
    }

    @Override
    public RunDelayIntervalManager<T, V> getInitializedDelayManager() {
        if (timer == null) {
            timer = new RunDelayIntervalManagerImpl<>(this);
        }
        return timer;
    }

    @Override
    public void setDelayManager(RunDelayIntervalManager<T, V> manager) {
        timer = manager;
    }

    @Override
    public RunCycleManager<T, V> getCycleManager() {
        return cycle;
    }

    @Override
    public void setCycleManager(RunCycleManager<T, V> manager) {
        cycle = manager;
    }

    @Override
    public RunCycleManager<T, V> getInitializedCycleManager() {
        if (cycle == null) {
            cycle = new RunCycleManagerImpl<>(this);
        }
        return cycle;
    }

    @Override
    public void setStackManager(RunActionStackManager<T, V> manager) {
        stack = manager;
    }

    @Override
    public RunActionStackManager<T, V> getStackManager() {
        return stack;
    }

    @Override
    public RunActionStackManager<T, V> getInitializedStackManager() {
        if (stack == null) {
            stack = new RunActionStackManagerImpl<>(this);
        }
        return stack;
    }

    @Override
    public void setConditionManager(RunCondManager<T, C, V> manager) {
        conds = manager;
    }

    @Override
    public RunCondManager<T, C, V> getConditionManager() {
        return conds;
    }

    @Override
    public RunCondManager<T, C, V> getInitializedConditionManager() {
        if (conds == null) {
            conds = new RunCondManagerImpl<>(this);
        }
        return conds;
    }

    @Override
    public boolean validateResult(C result, boolean isAction) {
        boolean retValue = true;
        if (conds != null) {
            RunCondition<T, C> current = conds.getCurrent();
            if (current != null) {
                current.setActionTest(isAction);
                current.setCurrentResult(result);
                if (current.next() == null) {
                    retValue = false;
                }
            }
        }
        return retValue;
    }

}
