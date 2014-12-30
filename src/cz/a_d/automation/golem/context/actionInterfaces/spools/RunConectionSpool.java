/*
 */
package cz.a_d.automation.golem.context.actionInterfaces.spools;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.spools.AbstractSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping AbstractSpool interface with definition used for storing connections.
 *
 * @author casper
 */
public interface RunConectionSpool extends AbstractSpool<Object, ConnectionKey, Connection> {

}
