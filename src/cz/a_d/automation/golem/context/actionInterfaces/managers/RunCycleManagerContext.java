/*
 */
package cz.a_d.automation.golem.context.actionInterfaces.managers;

import cz.a_d.automation.golem.interfaces.context.managers.RunCycleManager;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping list of RunCycleManager interface.
 *
 * @author casper
 */
public interface RunCycleManagerContext extends RunCycleManager<Object, Object> {

}
