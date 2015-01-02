/*
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.spools.keys.ParameterKey;

/**
 * Interface describe action parameter spool. Spool used to store and reuse parameter between actions in stream.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 * @param <V> the type of value used in ParameterSpool.
 */
public interface ParameterSpool<A, V> extends AbstractSpool<A, ParameterKey<?>, V> {

    @Override
    public ParameterSpool<A, V> newInstance();
}
