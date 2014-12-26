/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunCycle;

/**
 *
 * @author casper
 */
public interface RunCycleManager<T,V> extends ContextManager<T, RunCycle<T>, V> {

    public boolean setup(T action, long repeatCount, int actionCount);
    
    @Override
    public RunCycle<T> getCurrent();
}
