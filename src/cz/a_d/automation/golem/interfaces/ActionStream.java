/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces;

import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import java.util.List;

/**
 *
 * @author maslu02
 */
public interface ActionStream<A, V> extends Cloneable {

    public List<A> getActionList();

    public void setParameter(ParameterSpool<A, V> actionParams);

    public ParameterSpool<A, V> getParameterMap();

    public ResetableIterator<A> resetableIterator();

    public Object clone() throws CloneNotSupportedException;
}
