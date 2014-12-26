/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.RunContext;

/**
 *
 * @author casper
 */
public interface RunContextManagers<T, C, V> extends RunContext<T, V> {

    /**
     *
     * @param manager
     */
    public void setCycleManager(RunCycleManager<T, V> manager);

    /**
     *
     * @return
     */
    public RunCycleManager<T, V> getCycleManager();

    public RunCycleManager<T, V> getInitializedCycleManager();

    public void setDelayManager(RunDelayIntervalManager<T, V> manager);

    public RunDelayIntervalManager<T, V> getDelayManager();

    public RunDelayIntervalManager<T, V> getInitializedDelayManager();

    public void setStackManager(RunActionStackManager<T, V> manager);

    public RunActionStackManager<T, V> getStackManager();

    public RunActionStackManager<T, V> getInitializedStackManager();

    public void setConditionManager(RunCondManager<T, C, V> manager);

    public RunCondManager<T, C, V> getConditionManager();

    public RunCondManager<T, C, V> getInitializedConditionManager();

    public boolean validateResult(C result, boolean isAction);
}
