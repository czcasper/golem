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
import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.context.RunCycle;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Golem implementation of interface RunCycle. It is providing working implementation of cycles on the top of action stream. Cycles can be
 * managed by action inside cycle. Implementation is exposed by Runner context objects which can be injected into actions.
 *
 * @author casper
 * @param <T> the type of actions managed by cycle manager.
 * @param <V>
 */
public class RunCycleImpl<T, V> implements RunCycle<T>, Cloneable {

    /**
     * Variables releated range of cycle in array.
     */
    protected long repeatCount;

    /**
     * Index of currently processed action in cycle. Index is counted from first action with value 0 and reset to zero in every iteration.
     */
    protected int actionIndex;

    /**
     * Break logic flag
     */
    protected boolean breakFlag;

    /**
     * Instance of action which is first action of cycle from action stream.
     */
    protected T startAction;

    /**
     * Instance of action which is last action of cycle from action stream.
     */
    protected T endAction;

    /**
     * Index of current cycle iteration.
     */
    protected long cycleIterationNum;

    /**
     * List of actions from action stream which is used for iteration and implementing logic of cycle on top of action stream.
     */
    protected ActionStream<T, V> steps;

    /**
     * Iterator used to iterate actions from list of actions.
     */
    protected ResetableIterator<T> internalIt;

    /**
     * Constructing cycle implementation from list of actions and iterator connected with this list. List is wrapped in case when it is
     * needed to provide safe search for specific instance in list based on address of object.
     *
     * @param steps List of actions to be processed by current instance of cycle. Must be different from null and contains at least one
     *              action.
     * @throws NullPointerException  if the specified list is null
     * @throws IllegalStateException if the specified list is empty
     */
    public RunCycleImpl(ActionStream<T, V> steps) {
        if ((steps == null) || (internalIt == null)) {
            throw new NullPointerException("Run cycle cannot be initializet by null array or iterator");
        }
        this.steps = steps;
    }

    /**
     * Activating cycle on top of list defined in constructor.
     *
     * @param rootAction  instance of action which will trigger start of cycle. Must be different from null and inside list defined during
     *                    construction.
     * @param repeatCount amount of cycle iterations, negative value create infinite loop.
     * @param actionCount number of action in cycle. Must be greater or equal to zero. Used to finding last action in cycle.
     * @return true in case when cycle is properly initialized, otherwise false.
     */
    public boolean setupCycle(T rootAction, long repeatCount, int actionCount) {
        boolean retValue = false;
        if ((rootAction != null) && (steps.contains(rootAction) && (actionCount >= 0) && (repeatCount != 0))) {
            startAction = rootAction;
            endAction = steps.getAction(rootAction, actionCount);

            if (startAction != null && endAction != null) {
                this.repeatCount = repeatCount;
                this.cycleIterationNum = 0;
                this.actionIndex = 0;

                breakFlag = false;
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public void reset() {
        actionIndex = 0;
        cycleIterationNum = 0;
        breakFlag = false;
    }

    @Override
    public boolean hasNext() {
        if ((steps == null) || (steps.isEmpty()) || (breakFlag) || (internalIt == null)) {
            return false;
        }

        boolean retValue = false;
        if (repeatCount != 0) {
            if ((repeatCount > 0) && (cycleIterationNum < repeatCount)) {
                retValue = true;
            } else if (repeatCount < 0) {
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public T next() {
        T retValue = null;

        if (hasNext()) {
            actionIndex++;
            if (startAction != endAction) {
                retValue = internalIt.next();
                endCycleHandler(retValue);
            } else {
                cycleIterationNum++;
                retValue = endAction;
            }
        }
        return retValue;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T getStartAction() {
        return startAction;
    }

    @Override
    public boolean shiftStartAction(int index) {
        boolean retValue = false;
        if ((index != 0) && (!steps.isEmpty()) && (startAction != null)) {
            T action = steps.getAction(startAction, index);
            if(action!=null){
                startAction = action;
                retValue=true;
            }
        }
        return retValue;
    }

    @Override
    public T getEndAction() {
        return endAction;
    }

    @Override
    public boolean shiftEndAction(int index) {
        boolean retValue = false;
        if ((index != 0) && (steps != null) && (endAction != null) && (!steps.isEmpty())) {
            T action = steps.getAction(endAction, index);
            if(action!=null){
                endAction = action;
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public void setContinue() {
        Logger.getLogger(RunCycleImpl.class.getName()).log(Level.FINE, "Cycle has issued \"continue\" on cycle index::{0} in iteration:{1}", new Object[]{actionIndex, cycleIterationNum});
        cycleIterationNum++;
        if (hasNext()) {
            actionIndex = 0;
            updateIt(startAction);
        } else {
            updateIt(endAction);
            if (internalIt.hasNext()) {
                internalIt.next();
            }
        }
    }

    @Override
    public boolean isBreak() {
        return breakFlag;
    }

    @Override
    public void setBreak() {
        Logger.getLogger(RunCycleImpl.class.getName()).log(Level.FINE, "Cycle has issued \"break\" on cycle index::{0} in iteration:{1}", new Object[]{actionIndex, cycleIterationNum});
        if (hasNext()) {
            updateIt(endAction);
            if (internalIt.hasNext()) {
                internalIt.next();
            }
        }
        breakFlag = true;
    }

    @Override
    public long getCycleIterationNum() {
        return cycleIterationNum;
    }

    @Override
    public int getActionIndex() {
        return actionIndex;
    }

    @Override
    public long getRepeatCount() {
        return repeatCount;
    }

    @Override
    public void setRepeatCount(long repeatCount) {
        this.repeatCount = repeatCount;
    }

    @Override
    public boolean isFirstAction() {
        return (actionIndex == 0);
    }

    @Override
    public boolean isZeroLength() {
        return startAction == endAction;
    }

    @Override
    public void updateIt(T action) {
        Iterator<T> iterator = steps.iterator(action);
        if(iterator!=null){
            internalIt.setIt(iterator);
        }
    }

    @Override
    public void endCycleHandler(T action) {
        if ((action != null) && (action == endAction)) {
            cycleIterationNum++;
            if ((repeatCount < 0) || (cycleIterationNum < repeatCount)) {
                actionIndex = 0;
                updateIt(startAction);
            }
        }

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
