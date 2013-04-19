/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.actionInterfaces.managers;

import com.ca.automation.golem.context.actionInterfaces.RunCycleContext;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public interface  RunCycleManagerContext<T> {
    
    /**
     *
     * @param action
     * @param repeatCount
     * @param actionCount
     * @return
     */
    public boolean setup(T action, long repeatCount, int actionCount);
    /**
     *
     * @param action
     */
    public void load(T action);
    /**
     *
     * @return
     */
    public RunCycleContext getCurrent();

}
