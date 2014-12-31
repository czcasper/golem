/*
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunCondition;
import java.util.List;

/**
 * Interface describing method specific to manger of run condition.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 * @param <C> the type of object used for validation of run results
 */
public interface RunCondManager<T, C extends Object, V> extends ContextManager<T, RunCondition<T, C>, V> {

    /**
     * Registering of new instance of managed feature. This method has specific parameters for result validation feature. It is providing
     * same functionality like general version.
     *
     * @param action  instance of action from action stream which will trigger loading registered result validator instance.
     * @param expect  expected result from validated actions.
     * @param counter amount of action from action stream for which will be active result validation.
     *
     * @return true in case when new instance of managed feature is configured properly, otherwise return false.
     */
    public boolean setup(T action, C expect, long counter);

    @Override
    public RunCondition<T, C> getCurrent();

    /**
     * Getter for accessing list of currently active instances of result testers..
     *
     * @return list of active result tester currently processed by condition manager.
     */
    public List<RunCondition<T, C>> getActive();
}
