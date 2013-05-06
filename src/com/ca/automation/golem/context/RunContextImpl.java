/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.RunActionStackManager;
import com.ca.automation.golem.interfaces.RunCondManager;
import com.ca.automation.golem.interfaces.RunCondition;
import com.ca.automation.golem.interfaces.RunContextManagers;
import com.ca.automation.golem.interfaces.RunCycleManager;
import com.ca.automation.golem.interfaces.RunDelayIntervalManager;
import java.util.Iterator;

/**
 *
 * @param <T>
 * @author basad01
 */
public class RunContextImpl<T, C, K, V> implements RunContextManagers<T, C, K, V>, Iterator<T> {

    protected ActionStream<T, K, V> currentStream;
    private ResetableIterator<T> it;
    protected T currentAction;
    protected RunCycleManager<T, K, V> cycle;
    protected RunDelayIntervalManager<T, K, V> timer;
    protected RunActionStackManager<T, K, V> stack;
    protected RunCondManager<T, C, K, V> conds;

    @Override
    public void setActionStream(ActionStream<T, K, V> stream) {
        // TODO throw exception in case when stream is null
        this.currentStream = stream;
        it = currentStream.resetableIterator();
    }

    @Override
    public ActionStream<T, K, V> getActionStream() {
        return currentStream;
    }

    /**
     *
     * @return
     */
    public Object getCurrentAction() {
        return currentAction;
    }

    /**
     *
     * @param currentAction
     */
    public void setCurrentAction(T currentAction) {
        this.currentAction = currentAction;
    }

    /**
     *
     * @return
     */
    @Override
    public ResetableIterator<T> resetableIterator() {
        return currentStream.resetableIterator();
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
        currentAction = retValue;
        return retValue;
    }

    @Override
    public void remove() {
        if (it == null) {
            throw new IllegalStateException("Data array wasn't initialized.");
        }
        it.remove();
    }

    /**
     *
     * @return
     */
    @Override
    public RunDelayIntervalManager<T, K, V> getDelayManager() {
        return timer;
    }

    @Override
    public void setDelayManager(RunDelayIntervalManager<T, K, V> manager) {
        timer = manager;
    }

    /**
     *
     * @return
     */
    @Override
    public RunCycleManager<T, K, V> getCycleManager() {
        return cycle;
    }

    /**
     *
     * @param manager
     */
    @Override
    public void setCycleManager(RunCycleManager<T, K, V> manager) {
        cycle = manager;
    }

    /**
     *
     * @param manager
     */
    @Override
    public void setStackManager(RunActionStackManager<T, K, V> manager) {
        stack = manager;
    }

    /**
     *
     * @return
     */
    @Override
    public RunActionStackManager<T, K, V> getStackManager() {
        return stack;
    }    

    @Override
    public void setConditionManager(RunCondManager<T, C, K, V> manager) {
        conds = manager;
    }

    @Override
    public RunCondManager<T, C, K, V> getConditionManager() {
        return conds;
    }

    @Override
    public boolean validateResult(C result, boolean isAction) {
        boolean retValue = true;
        if(conds!=null){
            RunCondition<T, C> current = conds.getCurrent();
            if(current!=null){
                current.setActionTest(isAction);
                current.setCurrentResult(result);
                if(current.next()==null){
                    retValue = false;
                }
            }
        }
        return retValue;
    }
    
}
