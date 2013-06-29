/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools;

import com.ca.automation.golem.interfaces.connections.Connection;
import com.ca.automation.golem.interfaces.spools.keys.ConnectionKey;

/**
 *
 * @author maslu02
 */
public interface ConnectionSpool<A> extends AbstractSpool<A, ConnectionKey<?>, Connection> {
    
}
