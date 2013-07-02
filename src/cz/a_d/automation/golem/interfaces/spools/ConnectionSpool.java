/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;

/**
 *
 * @author maslu02
 */
public interface ConnectionSpool<A> extends AbstractSpool<A, ConnectionKey<?>, Connection> {
    
}
