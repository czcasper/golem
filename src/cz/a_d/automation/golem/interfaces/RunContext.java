/*
 */
package cz.a_d.automation.golem.interfaces;

import cz.a_d.automation.golem.common.iterators.ResetableIterator;

/**
 * Interface for connecting actions in stream with parameters used during action processing. This allow to define package of information for
 * runner and simplify sharing of context between managers.
 *
 * @author casper
 * @param <T> the type of actions managed by this context
 * @param <V> the type of value used in parameter spool.
 */
public interface RunContext<T, V> extends Iterable<T> {

    /**
     * Setter for action stream connected with this package of information for runner.
     *
     * @param stream instance of object implementing ActionStream interface to provide data for iteration of action to processing by runner.
     *               Must be different from null and cannot be empty.
     */
    public void setActionStream(ActionStream<T, V> stream);

    /**
     * Getter to share action stream connected with this package of information for runner. Used by managers to access actions in stream
     * directly.
     *
     * @return currently used instance of object implementing action stream interface to provide data for iteration. Can be null just in
     *         case when context is not fully initialized.
     */
    public ActionStream<T, V> getActionStream();

    /**
     * Providing access to iterator used for iteration of actions in stream. This is allowing managers to control flow of iteration and
     * change iterator of current stream or add additional actions into stream.
     *
     * @return instance of iterator used to iterate actions in action stream. Never returns null value.
     */
    public ResetableIterator<T> resetableIterator();
}
