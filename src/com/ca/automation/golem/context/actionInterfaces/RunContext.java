/*
 */
package com.ca.automation.golem.context.actionInterfaces;

import com.ca.automation.golem.context.actionInterfaces.managers.RunActionStackManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCycleManagerContext;
import com.ca.automation.golem.context.actionInterfaces.managers.RunDelayIntervalManagerContext;
import java.util.Iterator;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public interface RunContext<T extends Object> extends Iterable<T>, Iterator<T> {
    
    /**
     *
     * @return
     */
    public RunCycleManagerContext getCycleManager();
    /**
     *
     * @return
     */
    public RunDelayIntervalManagerContext getTimerManager();
    /**
     *
     * @return
     */
    public RunActionStackManagerContext getStackManager();
}
