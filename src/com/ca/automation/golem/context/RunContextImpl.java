/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.context.actionInterfaces.RunContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunActionStackManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCycleManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunDelayIntervalManagerContext;
import com.ca.automation.golem.context.managers.RunActionStackManager;
import com.ca.automation.golem.context.managers.RunCycleManager;
import com.ca.automation.golem.context.managers.RunDelayIntervalManager;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <T> 
 * @author basad01
 */
public class RunContextImpl<T> implements RunContext<T>{

    /*
     * 
     */
    /**
     *
     */
    protected List<T> steps;
    private ResetableIterator<T> it;
    /**
     *
     */
    protected T currentAction;

    /* Flag used in next and has next method for correct dealing with 
     * updating iterator by cycle
     */
    private boolean cycleNextFlag;
    private RunCycleManager<T> cycle;
    /*
     * 
     */
    private RunDelayIntervalManager<T> timer;
    /*
     * 
     */
    private RunActionStackManager<T> stack;
    /*
     * 
     */
//    RunTreeElement rootElement;
//    RunTreeElement currentElement;

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
    public List<T> getSteps() {
        return steps;
    }

    /**
     *
     * @param steps
     */
    public void setSteps(List<T> steps) {
        if (steps != null) {
            this.steps = steps;
            it = new ResetableIterator<T>(steps.iterator());
            currentAction = null;
        }
    }

    /**
     *
     * @param it
     */
    public void setIt(Iterator<T> it) {
        if (it instanceof ResetableIterator) {
            this.it = (ResetableIterator<T>) it;
        } else {
            this.it = new ResetableIterator<T>(it);
        }

    }

    /**
     *
     * @return
     */
    public ResetableIterator<T> getIt() {
        return it;
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
    public RunCycleManagerContext<T> getCycleManager() {
        return cycle;
    }

    /**
     *
     * @return
     */
    @Override
    public RunDelayIntervalManagerContext<T> getTimerManager() {
        return timer;
    }

    /**
     *
     * @param manager
     */
    public void setCycleManager(RunCycleManager<T> manager) {
        cycle = manager;
    }

    /**
     *
     * @param manager
     */
    public void setTimerManager(RunDelayIntervalManager<T> manager) {
        timer = manager;
    }

    /**
     *
     * @param manager
     */
    public void setStackManager(RunActionStackManager<T> manager) {
        stack = manager;
    }

    /**
     *
     * @return
     */
    @Override
    public RunActionStackManagerContext getStackManager() {
        return stack;
    }
}
