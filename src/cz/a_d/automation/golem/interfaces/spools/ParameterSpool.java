/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.spools.keys.ParameterKey;

/**
 *
 * @author casper
 */
public interface ParameterSpool<A, V> extends AbstractSpool<A, ParameterKey<?>, V> {

    @Override
    public ParameterSpool<A, V> newInstance();
}
