/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

/**
 *
 * @author maslu02
 */
public interface RunDelayInterval<T> extends CloneableIterator<T> {

    public T getAction();

    /**
     * Get current iteration number.
     *
     * @return curent iteration number
     */
    public long getActionCount();

    /**
     * Set the value of number of actions. This number is used for like
     * iteration end marker. Negative value timer will run like infinite loop
     * Zero value will ends run of this timer Positive value will run this by
     * number of times specified in value
     *
     * @param actionCount new value of actionCount
     */
    public void setActionCount(long actionCount);

    /**
     * Get the value of time interval used for delay.
     *
     * @return the value of time interval
     */
    public long getTime();
    
    /**
     * Set the value of time interval used for delay.
     *
     * @param time new value of time must be positive number.
     *
     * @return true if input time was valid, otherwise false
     */
    public boolean setTime(long time);
    
    /**
     * Method for save timer initialization.
     *
     * @param action = this parameter is optional and can be null. However can
     * be usefull in case when you want to identify timer in active timers
     * array.
     * @param actionCount = this parmeter is used for timer live cycle
     * configuration. Must be different from zero to setup timer correctelly. In
     * case when is negative timer will live in unlimited number of iteration.
     * In case when is positive timer will live number of time stored in value.
     * @param time = this parameter must be greather than zero. Time unit is
     * miliseconds and this time is used for sleeping current thread.
     *
     * @return true in case when timer is initialized correctelly, otherwise
     * false.
     */
    public boolean setupTimer(T action, long actionCount, long time);
    
    /**
     * Stop timer execution
     */
    public void stop();    
    
}