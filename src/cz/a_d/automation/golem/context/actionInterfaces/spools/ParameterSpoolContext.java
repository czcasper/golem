/*
 */
package cz.a_d.automation.golem.context.actionInterfaces.spools;

import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;

/**
 * Interface is wrapping usage generic types in Golem. This is from reason of more type save injection of context into actions. In this case
 * is wrapping list of ParameterSpool interface.
 *
 * @author casper
 */
public interface ParameterSpoolContext extends ParameterSpool<Object, Object> {
    
}
