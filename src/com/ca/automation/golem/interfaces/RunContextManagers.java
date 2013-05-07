/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

/**
 *
 * @author maslu02
 */
public interface RunContextManagers<T, C, K, V> extends RunContext<T, K, V> {

    /**
     *
     * @param manager
     */
    public void setCycleManager(RunCycleManager<T, K, V> manager);

    /**
     *
     * @return
     */
    public RunCycleManager<T, K, V> getCycleManager();

    public RunCycleManager<T, K, V> getInitializedCycleManager();

    public void setDelayManager(RunDelayIntervalManager<T, K, V> manager);

    public RunDelayIntervalManager<T, K, V> getDelayManager();

    public RunDelayIntervalManager<T, K, V> getInitializedDelayManager();

    public void setStackManager(RunActionStackManager<T, K, V> manager);

    public RunActionStackManager<T, K, V> getStackManager();

    public RunActionStackManager<T, K, V> getInitializedStackManager();

    public void setConditionManager(RunCondManager<T, C, K, V> manager);

    public RunCondManager<T, C, K, V> getConditionManager();

    public RunCondManager<T, C, K, V> getInitializedConditionManager();

    public boolean validateResult(C result, boolean isAction);
}
