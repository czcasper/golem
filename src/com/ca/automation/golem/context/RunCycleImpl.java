/*
 * 
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.interfaces.context.RunCycle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class cover data storage for information releated to Runner cycle
 * implementation. RunCycle interface is used for exposing method into runner
 * actions, this allows driving cycles directelly from actions inside cycle.
 *
 * @param <T>
 * @author maslu02
 */
public class RunCycleImpl<T> implements RunCycle<T>, Cloneable {

    /**
     * Variables releated range of cycle in array
     */
    private long repeatCount;
    private int actionIndex;
    /**
     * Break logic flag
     */
    private boolean breakFlag;
    /**
     * Start and end action for cycle
     */
    protected T startAction;
    /**
     *
     */
    protected T endAction;
    /**
     * Current iteration cycle
     */
    private long cycleIterationNum;
    /**
     * Variable used for initialize logic of cycle root and end action
     */
    private List<T> steps;
    private ResetableIterator<T> internalIt;

    /**
     *
     * @param steps
     */
    public RunCycleImpl(List<T> steps) {
        this(steps, new ResetableIterator<T>(steps.iterator()));
    }

    /**
     *
     * @param steps
     * @param internalIt
     */
    public RunCycleImpl(List<T> steps, ResetableIterator<T> internalIt) {
        if ((steps == null) || (internalIt == null)) {
            throw new NullPointerException("Run cycle cannot be initializet by null array or iterator");
        }
        if (steps.isEmpty()) {
            throw new IllegalStateException("Run cycle cannot be initialized by empty list");
        }

        if (!(steps instanceof AddressArrayList)) {
            steps = new AddressArrayList<T>(steps);
        }

        this.steps = steps;
        this.internalIt = internalIt;
    }

    /**
     * Setup cycle for curently used list and iterator.
     *
     * @param rootAction
     * @param repeatCount number of cycle iteration, -1 loop withouth end
     * @param actionCount number of action in this cycle. Must be greater or
     * equal to zero.
     * @return
     */
    public boolean setupCycle(T rootAction, long repeatCount, int actionCount) {
        boolean retValue = false;
        if ((rootAction != null) && (steps.contains(rootAction) && (actionCount >= 0) && (repeatCount != 0))) {
            int endPos = steps.indexOf(rootAction) + actionCount;
            if (endPos < steps.size()) {
                this.startAction = rootAction;
                endAction = steps.get(endPos);

                this.repeatCount = repeatCount;
                this.cycleIterationNum = 0;
                this.actionIndex = 0;

                breakFlag = false;
                retValue = true;
            }
        }
        return retValue;
    }

    /**
     * Method resets all cycle internal counters and flags. This allows start
     * cycle from scrach, or reuse cycle object for next run. Internal iterator
     * is not touched in this method.
     */
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

    /**
     * Get current cycle start action
     *
     * @return action used for start next iteration in cycle
     */
    @Override
    public T getStartAction() {
        return startAction;
    }

    /**
     * Move cycle start action by relative coordinate.
     *
     * @param index non zero value used for counting new possition of start
     * action relativly from current start action index. If start action doesn't
     * exist in current array then index is used absoluttely.
     *
     * @return true if update was succesfull otherwise false.
     */
    @Override
    public boolean shiftStartAction(int index) {
        boolean retValue = false;
        if ((index != 0) && (!steps.isEmpty()) && (startAction != null)) {
            int newIndex;
            if (steps.contains(startAction)) {
                newIndex = steps.indexOf(startAction) + index;
            } else {
                newIndex = index;
            }
            if ((newIndex >= 0) && (newIndex < steps.size())) {
                int endPos = steps.indexOf(endAction);
                if (endPos >= 0) {
                    if (newIndex <= endPos) {
                        startAction = steps.get(newIndex);
                        retValue = true;
                    }
                } else {
                    startAction = steps.get(newIndex);
                    retValue = true;
                }
            }
        }
        return retValue;
    }

    /**
     * Get the endAction object for this cycle
     *
     * @return the object which represent last action in cycle
     */
    @Override
    public T getEndAction() {
        return endAction;
    }

    /**
     * Move end cycle action by relative coordinate.
     *
     * @param index non zero value used for counting new possition of end action
     * relativly from current end action index. If start action doesn't exist in
     * current array then index is used absoluttely.
     *
     * @return true if update was succesfull otherwise false.
     */
    @Override
    public boolean shiftEndAction(int index) {
        boolean retValue = false;
        if ((index != 0) && (steps != null) && (endAction != null) && (!steps.isEmpty())) {
            int newIndex;
            if (steps.contains(endAction)) {
                newIndex = steps.indexOf(endAction) + index;
            } else {
                newIndex = index;
            }

            if ((newIndex >= 0) && (newIndex < steps.size())) {
                endAction = steps.get(newIndex);
                retValue = true;
            }
        }
        return retValue;
    }

    /**
     * Set continue status of cycle to true.
     */
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

    /**
     * Get break status of cycle.
     *
     * @return the value of breakFlag
     */
    @Override
    public boolean isBreak() {
        return breakFlag;
    }

    /**
     * Set break status of cycle.
     *
     */
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

    /**
     * Get current cycle iteration number.
     *
     * @return index of current iteration in this cycle
     */
    @Override
    public long getCycleIterationNum() {
        return cycleIterationNum;
    }

    /**
     * Get current cycle action index.
     *
     * @return index of action in cycle relative from cycle root action
     */
    @Override
    public int getActionIndex() {
        return actionIndex;
    }

    /**
     * Get current number of iteration which should be executed by this cycle.
     *
     * @return positive number if cycle has defined number of iteration, zero in
     * case when cycle will not run and negative for cycle without defined
     * number iterations
     */
    @Override
    public long getRepeatCount() {
        return repeatCount;
    }

    /**
     * Change current number of iteration in cycle, it will have affect in next
     * iteration.
     *
     * @param repeatCount number of iteration. If is zero it will stop cycle. In
     * case when is positive changed number of iteration used by this cycle for
     * comparing with current iteration index. Numbers less than zero made this
     * loop infinite.
     */
    @Override
    public void setRepeatCount(long repeatCount) {
        this.repeatCount = repeatCount;
    }

    /**
     * Test if current action is first action in cycle
     *
     * @return true in case when current cycle action is first action in cycle
     */
    @Override
    public boolean isFirstAction() {
        return (actionIndex == 0);
    }

    /**
     * This method test if cycle has startAction equal to endAction.
     *
     * @return true in case when cycle has just one action, otherwise false
     */
    @Override
    public boolean isZeroLength() {
        return startAction == endAction;
    }

    /**
     * Save method for updating curent interator container by action.
     *
     * @param action runner action object
     */
    @Override
    public void updateIt(T action) {
        if (steps.contains(action)) {
            int index = steps.indexOf(action);
            if (index >= 0) {
                if (index >= steps.size()) {
                    index = steps.size() - 1;
                    internalIt.setIt(steps.listIterator(index));
                    internalIt.next();
                } else {
                    internalIt.setIt(steps.listIterator(index));
                }
            }
        }
    }

    /**
     * This method move cycle in next loop in case when action is equal to
     * endAction.
     *
     * @param action curently processed action
     */
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
