/*
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 * Interface describe Action stream spool. Spool used to store and reuse action stream definitions.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 * @param <V> the type of value used in ParameterSpool.
 */
public interface ActionStreamSpool<A, V> extends AbstractSpool<A, ActionStreamKey<?>, ActionStream<A, V>> {

    @Override
    public ActionStreamSpool<A, V> newInstance();
}
