/*
 */
package cz.a_d.automation.golem.context.actionInterfaces;

import cz.a_d.automation.golem.interfaces.context.RunDelayInterval;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping of RunDelayInterval interface.
 *
 * @author casper
 */
public interface RunDelayIntervalContext extends RunDelayInterval<Object> {

}
