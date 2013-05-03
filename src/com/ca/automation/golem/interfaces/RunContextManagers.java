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
    public void setCycleManager(ContextManager<T,RunCycle<T>,K,V> manager);
    /**
     *
     * @return
     */
    public ContextManager<T,RunCycle<T>,K,V> getCycleManager();
    
    
}
