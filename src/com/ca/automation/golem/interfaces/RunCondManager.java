/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

import java.util.List;

/**
 *
 * @author maslu02
 */
public interface RunCondManager<T, C extends Object, K, V> extends ContextManager<T, RunCondition<T, C>, K, V> {

    public boolean setup(T action, C expect, long counter);

    @Override
    public RunCondition<T, C> getCurrent();

    public List<RunCondition<T, C>> getActive();
}
