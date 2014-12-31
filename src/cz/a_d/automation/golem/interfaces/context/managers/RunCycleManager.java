/*
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunCycle;

/**
 * Interface describing method specific to manger of run cycles.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 */
public interface RunCycleManager<T, V> extends ContextManager<T, RunCycle<T>, V> {

    /**
     * Registering of new instance of managed feature. This method has specific parameters for result validation feature. It is providing
     * same functionality like general version.
     *
     * @param action      instance of action from action stream which will trigger loading registered result validator instance.
     * @param repeatCount amount of cycle iterations, negative value create infinite loop.
     * @param actionCount number of action in cycle. Must be greater or equal to zero. Used to finding last action in cycle.
     * 
     * @return true in case when new instance of managed feature is configured properly, otherwise return false.
     */
    public boolean setup(T action, long repeatCount, int actionCount);

    @Override
    public RunCycle<T> getCurrent();
}
