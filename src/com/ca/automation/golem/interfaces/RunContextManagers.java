/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

/**
 *
 * @author maslu02
 */
public interface RunContextManagers<T,C,K,V> extends RunContext<T, K, V> {
    
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
    
    
    public void setConditionManager(RunCondManager<T,C,K,V> manager);
    
    public RunCondManager<T,C,K,V> getConditionManager();
    
    public boolean validateResult(C result,boolean isAction);
    
}
