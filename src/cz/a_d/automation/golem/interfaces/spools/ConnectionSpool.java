/*
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;

/**
 * Interface describe Action connection spool. Spool used to store and reuse connections used by active action streams.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 */
public interface ConnectionSpool<A> extends AbstractSpool<A, ConnectionKey<?>, Connection> {

}
