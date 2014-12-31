/*
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunDelayInterval;
import java.util.List;

/**
 * Interface describing method specific to manger of delay.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 */
public interface RunDelayIntervalManager<T, V> extends ContextManager<T, RunDelayInterval<T>, V> {
    /**
     * Registering of new instance of managed feature. This method has specific parameters for result validation feature. It is providing
     * same functionality like general version.
     *
     * @param action      instance of action from action stream which will trigger loading registered result validator instance.
     * @param time        define timer live cycle configuration. Must be different from zero to setup timer correctly. For negative integer
     *                    be active in unlimited number of iteration. Positive integer make it active for number of time stored in value.
     * @param actionCount this parmeter is used for timer live cycle configuration. Must be different from zero to setup timer correctelly.
     *                    In case when is negative timer will live in unlimited number of iteration. In case when is positive timer will
     *                    live number of time stored in value.
     *
     * @return true in case when new instance of managed feature is configured properly, otherwise return false.
     */
    public boolean setup(T action, long actionCount, long time);

    @Override
    public RunDelayInterval<T> getCurrent();

    /**
     * Getter for accessing list of currently active instances of run delay.
     *
     * @return list of active delay currently processed by run delay manager.
     */
    public List<RunDelayInterval<T>> getActive();
}
