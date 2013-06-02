/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.context.managers;

import com.ca.automation.golem.interfaces.context.RunCycle;

/**
 *
 * @author maslu02
 */
public interface RunCycleManager<T,V> extends ContextManager<T, RunCycle<T>, V> {

    public boolean setup(T action, long repeatCount, int actionCount);
    
    @Override
    public RunCycle<T> getCurrent();
}
