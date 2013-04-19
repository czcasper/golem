/*
 */
package com.ca.automation.golem.context.actionInterfaces;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public interface RunDelayIntervalContext<T> {

    /**
     * Get the timer start action
     *
     * @return the value of name
     */
    public T getAction();
        
    /**
     * Get current iteration number.
     *
     * @return curent iteration number
     */
    long getActionCount();

    /**
     * Get the value of time interval used for delay.
     *
     * @return the value of time interval
     */
    long getTime();

    /**
     * Reset internal timer iterator to zero.
     *
     */
    void reset();

    /**
     * Set the value of number of actions.
     * This number is used for like iteration end marker.
     * Negative value timer will run like infinite loop
     * Zero value will ends run of this timer
     * Positive value will run this by number of times specified in value
     *
     * @param actionCount new value of actionCount
     */
    void setActionCount(long actionCount);

    /**
     * Set the value of time interval used for delay.
     *
     * @param time new value of time must be positive number.
     *
     * @return true if input time was valid, otherwise false
     */
    boolean setTime(long time);
    
}
