/*
 */
package cz.a_d.automation.golem.interfaces;

import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import java.util.List;

/**
 * Interface describing all methods required by Golem runner. This methods are used during action stream processing and execution of action
 * methods.
 *
 * @author casper
 * @param <A> the type of actions managed by this steam.
 * @param <V> the type of value used in parameter spool.
 */
public interface ActionStream<A, V> extends Cloneable {

    /**
     * Getter to access list of actions defined by action stream.
     *
     * @return instance of list with actions. Must contains at least one valid action to be successfully consumed by runner.
     */
    public List<A> getActionList();

    /**
     * Changing spool of parameters used by action stream.
     *
     * @param actionParams instance of parameter spool. Must be different from null.
     */
    public void setParameterSpool(ParameterSpool<A, V> actionParams);

    /**
     * Getter to access currently used instance of parameter spool.
     *
     * @return instance of parameter spool. Never return null value.
     */
    public ParameterSpool<A, V> getParameterSpool();

    /**
     * Getter to access instance of iterator used for iteration actions in stream.
     *
     * @return instance of iterator which is providing access to actions in stream. Never return null value.
     */
    public ResetableIterator<A> resetableIterator();

    /**
     * Supports cloning of action stream. This allows duplicate action streams and run it in parallel.
     *
     * @return deep copy of action stream including all actions and parameters stored inside stream.
     * @throws CloneNotSupportedException if some object stored in action stream does not supports cloning.
     */
    public Object clone() throws CloneNotSupportedException;
}
