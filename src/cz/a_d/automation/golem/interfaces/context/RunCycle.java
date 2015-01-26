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
package cz.a_d.automation.golem.interfaces.context;

/**
 * Interface describing cycle feature provided by Golem on top of action stream. Cycles are triggered by specific action in stream and can
 * be infinite or with specific number of repeating. Also first and end action of cycle can be changed. Break and continue features of cycle
 * are required in this implementation.
 *
 * @author casper
 * @param <T> the type of actions managed by cycle manager.
 */
public interface RunCycle<T> extends CloneableIterator<T> {

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
    public T getEndAction();

    /**
     * Get current number of iteration which should be executed by this cycle
     *
     * @return positive number if cycle has defined number of iteration, zero in case when cycle will not run and negative for cycle without
     *         defined number iterations
     */
    public long getRepeatCount();

    /**
     * Get current cycle start action
     *
     * @return action used for start next iteration in cycle
     */
    public T getStartAction();

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
     * Change current number of iteration in cycle, it will have affect in next iteration.
     *
     * @param repeatCount number of iteration. If is zero it will stop cycle. In case when is positive change number of iteration used by
     *                    this cycle for counting iteration, and finnaly negative number made this loop infinite
     */
    public void setRepeatCount(long repeatCount);

    /**
     * Move end cycle action by relative coordinate.
     *
     * @param index non zero value used for counting new possition of end action relativly from current end action index. If start action
     *              doesn't exist in current array then index is used absoluttely.
     *
     * @return true if update was succesfull otherwise false.
     */
    public boolean shiftEndAction(int index);

    /**
     * Move cycle start action by relative coordinate.
     *
     * @param index non zero value used for counting new possition of start action relativly from current start action index. If start
     *              action doesn't exist in current array then index is used absoluttely.
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

    /**
     * This method test if cycle has startAction equal to endAction.
     *
     * @return true in case when cycle has just one action, otherwise false
     */
    public boolean isZeroLength();

    /**
     * Save method for updating curent interator container by action.
     *
     * @param action runner action object
     */
    public void updateIt(T action);

    /**
     * This method move cycle in next loop in case when action is equal to endAction.
     *
     * @param action curently processed action
     */
    public void endCycleHandler(T action);

    /**
     * Method resets all cycle internal counters and flags. This allows start cycle from scrach, or reuse cycle object for next run.
     * Internal iterator is not touched in this method.
     */
    public void reset();

}
