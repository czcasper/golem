/*
 */
package cz.a_d.automation.golem.interfaces.context;

/**
 * Interface for condition for validation result of action methods. Allow mapping of condition validations for triggered by specific action
 * and limited by count of methods which will be validated for result collected from return value of tested method.
 *
 * @author casper
 * @param <T> the type of action used for triggering result validation.
 * @param <C> the type of method return value used for result validation.
 */
public interface RunCondition<T, C> extends CloneableIterator<T> {

    /**
     * Configure validation for implementation of run result validation.
     *
     * @param action  action from action stream which is trigger for starting result validation.
     * @param expect  expected value returned by tested method
     * @param counter counter can be defined in two different modes.Positive integer is used and decremented by every validation until is
     *                zero. Any negative integer means validation is active until end of action stream.
     * @return true in case when setup has been successful, otherwise false.
     */
    public boolean setupCond(T action, C expect, long counter);

    /**
     * Getter for count of remaining check of actions.
     *
     * @return zero in case when validation is not active. Negative integer in case when validation is active for unlimited number of
     *         methods, positive integer in case when there is count down for active validation.
     */
    public long getActiveCounter();

    /**
     * Setter for controlling counter which is managing testing of result from action method.
     *
     * @param activeCounter zero for suspending validation. Negative integer to make validation active for unlimited number of methods,
     *                      positive integer change amount of method for which will be validation active.
     */
    public void setActiveCounter(long activeCounter);

    /**
     * Getter to access expected value of result. This value is used to test against return value of action method.
     *
     * @return instance of object used for method result validation.
     */
    public C getExpectedResult();

    /**
     * Setter which allows modification of expected result returned by successfully finished action method.
     *
     * @param expectedResult instance of object which will be used for comparison with value returned by action method. Null value is also
     *                       allowed.
     */
    public void setExpectedResult(C expectedResult);

    /**
     * Setter to configure value returned from action method. This value will be used for validation if action is successful or failed.
     *
     * @param currentResult instance of object returned by action method. Null value is allowed.
     */
    public void setCurrentResult(C currentResult);

    /**
     * Setter to allow change flag for skipping decrementing of validation active counter in case when there is set positive integer in
     * active counter.
     *
     * @param actionTest true for activating of decrement, false for disabling.
     */
    public void setActionTest(boolean actionTest);
}
