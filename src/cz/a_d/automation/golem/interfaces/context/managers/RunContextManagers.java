/*
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.RunContext;

/**
 * Interface describing interaction between particular Golem features. Single feature is implemented by manager of feature. Every feature is
 * separated from each other and loaded on demand of actions. Class is also keeping information about Action stream which is currently
 * executed to provide option for managers to change in flow of action stream.
 *
 * @author casper
 * @param <T> the type of actions managed by this context
 * @param <C> the type of value used for result validation.
 * @param <V> the type of value used in parameter spool.
 */
public interface RunContextManagers<T, C, V> extends RunContext<T, V> {

    /**
     * Changing of implementation for Cycle manager, used by this context implementation to provide cycle features on top of action stream.
     *
     * @param manager instance of object implementing RunCycle interface to provide feature of cycles. Must be different from null.
     */
    public void setCycleManager(RunCycleManager<T, V> manager);

    /**
     * Getter to access currently used instance of RunCycleManager implementation.
     *
     * @return instance of object which is implementing RunCycleManager interface to provide feature of cycles. Can return null value in
     *         case when context doesn't use cycle management.
     */
    public RunCycleManager<T, V> getCycleManager();

    /**
     * Getter to access currently used instance of RunCycleManager implementation. In case when context doesn't have initialized instance of
     * manager method will initialize it by default Golem implementation.
     *
     * @return instance of object implementing RunCycleManager interface to provide feature of cycles. Never is null value.
     */
    public RunCycleManager<T, V> getInitializedCycleManager();

    /**
     * Changing of implementation for Delay manager, used by this context implementation to provide waiting features on top of action
     * stream.
     *
     * @param manager instance of object which is implementing RunDelayIntervalManager interface to provide feature of waiting. Must be
     *                different from null.
     */
    public void setDelayManager(RunDelayIntervalManager<T, V> manager);

    /**
     * Getter to access currently used instance of RunDelayIntervalManager implementation.
     *
     * @return instance of object which is implementing RunDelayIntervalManager interface to provide feature of waiting. Can return null
     *         value in case when context doesn't use delay management.
     */
    public RunDelayIntervalManager<T, V> getDelayManager();

    /**
     * Getter to access currently used instance of RunDelayIntervalManager implementation. In case when context does not have initialized
     * instance of manager method will initialize it by default Golem implementation.
     *
     * @return instance of object implementing RunDelayIntervalManager interface to provide feature of cycles. Never is null value.
     */
    public RunDelayIntervalManager<T, V> getInitializedDelayManager();

    /**
     * Changing of implementation for Stack manager, used by this context implementation to provide action stack features on top of action
     * stream.
     *
     * @param manager instance of object which is implementing RunActionStackManager interface to provide feature of action stack. Must be
     *                different from null.
     */
    public void setStackManager(RunActionStackManager<T, V> manager);

    /**
     * Getter to access currently used instance of RunActionStackManager implementation.
     *
     * @return instance of object which is implementing RunActionStackManager interface to provide feature of action stack. Can return null
     *         value in case when context doesn't use action stack management.
     */
    public RunActionStackManager<T, V> getStackManager();

    /**
     * Getter to access currently used instance of RunActionStackManager implementation. In case when context does not have initialized
     * instance of manager method will initialize it by default Golem implementation.
     *
     * @return instance of object implementing RunActionStackManager interface to provide feature of cycles. Never is null value.
     */
    public RunActionStackManager<T, V> getInitializedStackManager();

    /**
     * Changing of implementation for Validation manager, used by this context implementation to provide result validation features on top
     * of action stream.
     *
     * @param manager instance of object which is implementing RunCondManager interface to provide feature of result validation. Must be
     *                different from null.
     */
    public void setConditionManager(RunCondManager<T, C, V> manager);

    /**
     * Getter to access currently used instance of RunCondManager implementation.
     *
     * @return instance of object which is implementing RunCondManager interface to provide feature of action result validation. Can return
     *         null value in case when context doesn't use action stack management.
     */
    public RunCondManager<T, C, V> getConditionManager();

    /**
     * Getter to access currently used instance of RunCondManager implementation. In case when context does not have initialized instance of
     * manager method will initialize it by default Golem implementation.
     *
     * @return instance of object implementing RunCondManager interface to provide feature of cycles. Never is null value.
     */
    public RunCondManager<T, C, V> getInitializedConditionManager();

    /**
     * Method used by Runner to provide validation on action method level and allows to stop execution process of action stream.
     *
     * @param result   value returned by action method to validation.
     * @param isAction flag to control counter on condition manager. In case when is true decreasing of counter is active.
     *
     * @return true in case when validation is successful and next action from stream should be processed, otherwise false.
     */
    public boolean validateResult(C result, boolean isAction);
}
