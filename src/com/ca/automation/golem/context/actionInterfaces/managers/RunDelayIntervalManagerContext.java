/*
 */
package com.ca.automation.golem.context.actionInterfaces.managers;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public interface RunDelayIntervalManagerContext<T> {

    /**
     * Safelly initialize all timer members by calling one method.
     *
     * @param action = action where timer will start working
     * @param time = value greather than 0. This value used for sleeping current
     * thread is in miliseconds.
     * @param actionCount = this parmeter is used for timer live cycle
     * configuration. Must be different from zero to setup timer correctelly. In
     * case when is negative timer will live in unlimited number of iteration.
     * In case when is positive timer will live number of time stored in value.
     * @return true in case when timer is initialized correctelly, otherwise
     * false.
     */
    boolean setup(T action, long time, long actionCount);
}
