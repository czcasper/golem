/*
 */
package cz.a_d.automation.golem.context.actionInterfaces;

import cz.a_d.automation.golem.interfaces.context.RunCycle;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping of RunCycle interface.
 *
 * @author casper
 */
public interface RunCycleContent extends RunCycle<Object> {

}
