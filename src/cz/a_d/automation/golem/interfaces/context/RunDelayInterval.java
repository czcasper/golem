/*
 */
package cz.a_d.automation.golem.interfaces.context;

/**
 * Interface describing delay feature of Golem. Delay is triggered by specified action from action stream. Waiting can be defined for fixed
 * number of actions of for unlimited number of them.
 *
 * @author casper
 * @param <T> the type of actions managed by delay manager.
 */
public interface RunDelayInterval<T> extends CloneableIterator<T> {

    /**
     * Getter to access action which is triggering delay feature.
     *
     * @return instance of Action from action stream after this action is wait feature triggered. Null value can be returned just in case
     *         when instance is not properly initialized.
     */
    public T getAction();

    /**
     * Getter for current iteration number.
     *
     * @return curent iteration number.
     */
    public long getActionCount();

    /**
     * Changing the value of number of actions for which is delay feature active. This number is used for marking end of iteration. Negative
     * integer value will lead to infinite loop. Zero value will ends run of this timer. Positive value will limit activity of delay for
     * number of times specified in value.
     *
     * @param actionCount new value of actionCount.
     */
    public void setActionCount(long actionCount);

    /**
     * Getter for value of time interval used for delay.
     *
     * @return the value of time interval in milliseconds.
     */
    public long getTime();

    /**
     * Changing the value of time interval used for delay.
     *
     * @param time new value of time delay in milliseconds, must be positive number greater then zero.
     *
     * @return true if input time interval was valid and change is correctly done, otherwise false.
     */
    public boolean setTime(long time);

    /**
     * Activate and configure delay feature on the top of action stream.
     *
     * @param action      is optional and can be null. However can be useful in case when you want to identify timer in active timers array.
     * @param actionCount define timer live cycle configuration. Must be different from zero to setup timer correctly. For negative integer
     *                    be active in unlimited number of iteration. Positive integer make it active for number of time stored in value.
     * @param time        must be greater than zero. Time is milliseconds and for this amount of milliseconds is current thread bring into
     *                    sleep mode after every action.
     *
     * @return true in case when timer is initialized correctly, otherwise false.
     */
    public boolean setupTimer(T action, long actionCount, long time);

    /**
     * Stopping timer execution. This will terminate delay realized by this timer directly after call of this method.
     */
    public void stop();

}
