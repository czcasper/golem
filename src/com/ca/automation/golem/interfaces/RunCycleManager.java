/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

/**
 *
 * @author maslu02
 */
public interface RunCycleManager<T,K,V> extends ContextManager<T, RunCycle<T>, K, V> {

    public boolean setup(T action, long repeatCount, int actionCount);
    
    @Override
    public RunCycle<T> getCurrent();
}
