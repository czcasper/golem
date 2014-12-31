/*
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import java.util.Iterator;

/**
 * Interface describing abstraction of all context managers. All common functions provided by content manager are defined in this interface.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <M> the type of manged feature.
 * @param <V> the type of value used in parameter spool.
 */
public interface ContextManager<T, M extends Iterator<T>, V> extends Iterator<T> {

    /**
     * Configure feature in manager, by using provided parameters. This method can be called multiple time and manager must cover this
     * option and allow definition of multiple active feature in parallel.
     *
     * @param action instance used to trigger feature configured by manager.
     * @param parms  parameters used by manager to configure managed feature.
     *
     * @return true in case when new instance of managed feature is configured properly, otherwise return false.
     */
    public boolean setup(T action, Object... parms);

    /**
     * Handling feature of triggering for managed feature.
     *
     * @param action currently processed action to identify if there should be some instance of managed feature started.
     */
    public void load(T action);

    /**
     * Getter to access current instance of managed feature.
     *
     * @return currently used instance of managed feature.Can be null in case when manager is not active.
     */
    public M getCurrent();
}
