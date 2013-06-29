/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools;

import com.ca.automation.golem.interfaces.spools.keys.ParameterKey;

/**
 *
 * @author maslu02
 */
public interface ParameterSpool<A,V> extends AbstractSpool<A, ParameterKey<?>, V> {
    @Override
    public ParameterSpool<A, V> newInstance();
}
