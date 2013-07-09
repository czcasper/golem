/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 *
 * @author maslu02
 */
public interface ActionStreamSpool<A, V> extends AbstractSpool<A, ActionStreamKey<?>, ActionStream<A, V>> {

    @Override
    public ActionStreamSpool<A, V> newInstance();
}
