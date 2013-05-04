/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

/**
 *
 * @author maslu02
 */
public interface RunContextManagers<T,K,V> extends RunContext<T, K, V> {
    
    /**
     *
     * @param manager
     */
    public void setCycleManager(RunCycleManager<T,K,V> manager);
    /**
     *
     * @return
     */
    public RunCycleManager<T,K,V> getCycleManager();
    
    public void setDelayManager(RunDelayIntervalManager<T,K,V> manager);
    
    public RunDelayIntervalManager<T,K,V> getDelayManager();
    
    public void setStackManager(RunActionStackManager<T, K, V> manager);
    
    public RunActionStackManager<T, K, V> getStackManager();
}
