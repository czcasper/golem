/*
 * 
 */
package cz.a_d.automation.golem.context;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.interfaces.context.RunCycle;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Golem implementation of interface RunCycle. It is providing working implementation of cycles on the top of action stream. Cycles can be
 * managed by action inside cycle. Implementation is exposed by Runner context objects which can be injected into actions.
 *
 * @author casper
 * @param <T> the type of actions managed by cycle manager.
 */
public class RunCycleImpl<T> implements RunCycle<T>, Cloneable {

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
    protected List<T> steps;

    /**
     * Iterator used to iterate actions from list of actions.
     */
    protected ResetableIterator<T> internalIt;

    /**
     * Construct Cycle implementation from list of actions. List is wrapped in case when it is needed to provide safe search for specific
     * instance in list based on address of object.
     *
     * @param steps List of actions to be processed by current instance of cycle. Must be different from null and contains at least one
     *              action.
     * @throws NullPointerException  if the specified list is null
     * @throws IllegalStateException if the specified list is empty
     */
    public RunCycleImpl(List<T> steps) {
        this(steps, new ResetableIterator<>(steps.iterator()));
    }

    /**
     * Constructing cycle implementation from list of actions and iterator connected with this list. List is wrapped in case when it is
     * needed to provide safe search for specific instance in list based on address of object.
     *
     * @param steps      List of actions to be processed by current instance of cycle. Must be different from null and contains at least one
     *                   action.
     * @param internalIt
     * @throws NullPointerException  if the specified list is null
     * @throws IllegalStateException if the specified list is empty
     */
    public RunCycleImpl(List<T> steps, ResetableIterator<T> internalIt) {
        if ((steps == null) || (internalIt == null)) {
            throw new NullPointerException("Run cycle cannot be initializet by null array or iterator");
        }
        if (steps.isEmpty()) {
            throw new IllegalStateException("Run cycle cannot be initialized by empty list");
        }

        if (!(steps instanceof AddressArrayList)) {
            steps = new AddressArrayList<>(steps);
        }

        this.steps = steps;
        this.internalIt = internalIt;
    }

    /**
     * Activating cycle on top of list defined in constructor.
     *
     * @param rootAction  instance of action which will trigger start of cycle. Must be different from null and inside list defined during
     *                    construction.
     * @param repeatCount amount of cycle iterations, negative value create infinite loop.
     * @param actionCount number of action in cycle. Must be greater or equal to zero. Used to finding last action in cycle.
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

    @Override
    public T getEndAction() {
        return endAction;
    }

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
