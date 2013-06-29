/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools;

import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 *
 * @author maslu02
 */
public interface ActionStreamSpool<A, V> extends AbstractSpool<A, ActionStreamKey<?>, ActionStream<A, V>> {
    
}
