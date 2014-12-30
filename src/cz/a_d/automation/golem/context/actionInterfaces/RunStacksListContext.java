/*
 */
package cz.a_d.automation.golem.context.actionInterfaces;

import java.util.List;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping list of RunActionStackContext interface.
 *
 * @author casper
 */
public interface RunStacksListContext extends List<RunActionStackContext> {

}
