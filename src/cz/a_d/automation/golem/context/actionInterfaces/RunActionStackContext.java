/*
 */
package cz.a_d.automation.golem.context.actionInterfaces;

import cz.a_d.automation.golem.interfaces.context.RunActionStack;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping of RunActionStack interface.
 *
 * @author casper
 */
public interface RunActionStackContext extends RunActionStack<Object> {
}
