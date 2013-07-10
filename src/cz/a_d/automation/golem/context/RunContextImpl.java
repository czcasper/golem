/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * This class is supporting all runner special context features. For managing 
 * features are used special manages implemented in Golem. All information connected
 * with What?(actions) and Which parameters are used? are stored in ActionStream.
 * 
 * 
 * @author maslu02
 * @param <T> - type for action objects
 * @param <C> - type used for validating result of actions
 * @param <V> - type used in parameter spool like value
 */
public class RunContextImpl<T, C, V> implements RunContextManagers<T, C, V>, Iterator<T> {

    protected ActionStream<T, V> currentStream;
    private ResetableIterator<T> it;
    protected T currentAction;
    protected RunCycleManager<T, V> cycle;
    protected RunDelayIntervalManager<T, V> timer;
    protected RunActionStackManager<T, V> stack;
    protected RunCondManager<T, C, V> conds;

    @Override
    public void setActionStream(ActionStream<T, V> stream) {
        if(stream==null){
            throw new NullPointerException("RunContext cannot be intilized by null ActionStream");
        }
        this.currentStream = stream;
        it = currentStream.resetableIterator();
    }

    @Override
    public ActionStream<T, V> getActionStream() {
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
        ResetableIterator<T> retValue = null;
        if(currentStream!=null){
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
    public RunDelayIntervalManager<T, V> getDelayManager() {
        return timer;
    }

    @Override
    public RunDelayIntervalManager<T, V> getInitializedDelayManager() {
        if(timer==null){
            timer = new RunDelayIntervalManagerImpl<>(this);
        }
        return timer;
    }

    @Override
    public void setDelayManager(RunDelayIntervalManager<T, V> manager) {
        timer = manager;
    }

    /**
     *
     * @return
     */
    @Override
    public RunCycleManager<T, V> getCycleManager() {
        return cycle;
    }

    /**
     *
     * @param manager
     */
    @Override
    public void setCycleManager(RunCycleManager<T, V> manager) {
        cycle = manager;
    }

    @Override
    public RunCycleManager<T, V> getInitializedCycleManager() {
        if(cycle==null){
            cycle = new RunCycleManagerImpl<>(this);
        }
        return cycle;
    }

    /**
     *
     * @param manager
     */
    @Override
    public void setStackManager(RunActionStackManager<T, V> manager) {
        stack = manager;
    }

    /**
     *
     * @return
     */
    @Override
    public RunActionStackManager<T, V> getStackManager() {
        return stack;
    }    

    @Override
    public RunActionStackManager<T, V> getInitializedStackManager() {
        if(stack == null){
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
        if(conds == null){
            conds = new RunCondManagerImpl<>(this);
        }
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
