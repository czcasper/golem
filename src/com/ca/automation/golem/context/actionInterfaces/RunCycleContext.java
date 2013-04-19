/*
 */
package com.ca.automation.golem.context.actionInterfaces;

/**
 *
 * @author maslu02
 */
public interface RunCycleContext {

    /**
     * Get current action index in cycle
     *
     * @return index of action in cycle relative from cycle root action
     */
    public int getActionIndex();

    /**
     * Get current cycle iteration number
     *
     * @return index of current iteration in this cycle
     */
    public long getCycleIterationNum();

    /**
     * Get the endAction object for this cycle
     *
     * @return the object which represent last action in cycle
     */
    public Object getEndAction();

    /**
     * Get current number of iteration which should be executed by this cycle
     *
     * @return positive number if cycle has defined number of iteration, zero in
     * case when cycle will not run and negative for cycle without defined
     * number iterations
     */
    public long getRepeatCount();

    /**
     * Get current cycle start action
     *
     * @return action used for start next iteration in cycle
     */
    public Object getStartAction();

    /**
     * Get break cycle flag
     *
     * @return the value of breakFlag
     */
    public boolean isBreak();

    /**
     * Set break status of cycle flag to true.
     */
    public void setBreak();

    /**
     * Set continue status of cycle flag to true.
     */
    public void setContinue();

    /**
     * Change current number of iteration in cycle, it will have affect in next
     * iteration.
     *
     * @param repeatCount number of iteration. If is zero it will stop cycle. In
     * case when is positive change number of iteration used by this cycle for
     * counting iteration, and finnaly negative number made this loop infinite
     */
    public void setRepeatCount(long repeatCount);

    /**
     * Move end cycle action by relative coordinate.
     *
     * @param index non zero value used for counting new possition of end action
     * relativly from current end action index. If start action doesn't exist in
     * current array then index is used absoluttely.
     *
     * @return true if update was succesfull otherwise false.
     */
    public boolean shiftEndAction(int index);

    /**
     * Move cycle start action by relative coordinate.
     *
     * @param index non zero value used for counting new possition of start
     * action relativly from current start action index. If start action doesn't
     * exist in current array then index is used absoluttely.
     *
     * @return true if update was succesfull otherwise false.
     */
    public boolean shiftStartAction(int index);

    /**
     * Test if current action is first action in cycle
     *
     * @return true in case when current cycle action is first action in cycle
     */
    public boolean isFirstAction();
}
