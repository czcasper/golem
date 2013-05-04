/*
 * 
 */
package com.ca.automation.golem.context;


import com.ca.automation.golem.interfaces.DelayInterval;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public class RunDelayIntervalImpl<T> implements DelayInterval<T>, Cloneable {

    /**
     *
     */
    protected long time = 0;
    /**
     *
     */
    protected long actionCount = 0;
    /**
     *
     */
    protected long iterator = 0;
    /**
     *
     */
    protected T action;

    @Override
    public boolean hasNext() {
        boolean retValue = false;
        if ((time != 0) && (actionCount != 0) && (actionCount != iterator)) {
            if ((actionCount < 0) || ((iterator < actionCount))) {
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public T next() {
        T retValue = null;
        if (hasNext()) {
            iterator++;
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(RunDelayIntervalImpl.class.getName()).log(Level.SEVERE, "Waiting process was interupted by external source. For current action:" + action.toString(), ex);
            }
            retValue = action;
        }
        return retValue;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
    @Override
    public boolean setupTimer(T action, long actionCount, long time) {
        boolean retValue = false;
        if ((actionCount != 0) && (setTime(time))) {
            this.action = action;
            this.actionCount = actionCount;
            this.iterator = 0;
            retValue = true;
        }
        return retValue;
    }

    @Override
    public T getAction() {
        return action;
    }

    /**
     * Get current iteration number.
     *
     * @return curent iteration number
     */
    @Override
    public long getActionCount() {
        return iterator;
    }

    /**
     * Set the value of number of actions. This number is used for like
     * iteration end marker. Negative value timer will run like infinite loop
     * Zero value will ends run of this timer Positive value will run this by
     * number of times specified in value
     *
     * @param actionCount new value of actionCount
     */
    @Override
    public void setActionCount(long actionCount) {
        this.actionCount = actionCount;
    }

    /**
     * Reset internal timer iterator to zero.
     *
     */
//    @Override
//    public void reset() {
//        iterator = 0;
//    }

    /**
     * Stop timer execution
     */
    @Override
    public void stop() {
        iterator = actionCount;
    }

    /**
     * Get the value of time interval used for delay.
     *
     * @return the value of time interval
     */
    @Override
    public long getTime() {
        return time;
    }

    /**
     * Set the value of time interval used for delay.
     *
     * @param time new value of time must be positive number.
     *
     * @return true if input time was valid, otherwise false
     */
    @Override
    public boolean setTime(long time) {
        boolean retValue = false;
        if (time > 0) {
            this.time = time;
            retValue = true;
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public RunDelayIntervalImpl<T> clone() throws CloneNotSupportedException {
        RunDelayIntervalImpl<T> retValue = (RunDelayIntervalImpl<T>) super.clone();
        return retValue;
    }
}
