/*
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunActionStack;
import java.util.List;

/**
 * Interface describing method specific to manger of run action stack.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 */
public interface RunActionStackManager<T, V> extends ContextManager<T, RunActionStack<T>, V> {

    @Override
    public RunActionStack<T> getCurrent();

    /**
     * Getter for accessing list of currently active instances of stack.
     *
     * @return list of active stack currently processed by stack manager.
     */
    public List<RunActionStack<T>> getActive();
}
