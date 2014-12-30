/*
 */
package cz.a_d.automation.golem.context.actionInterfaces.managers;

import cz.a_d.automation.golem.interfaces.context.managers.RunCondManager;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping list of RunCondManager interface.
 *
 * @author casper
 */
public interface RunCondManagerContext extends RunCondManager<Object, Boolean, Object> {

}
