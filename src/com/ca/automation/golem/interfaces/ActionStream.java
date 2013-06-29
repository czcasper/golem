/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.ParameterSpool;
import com.ca.automation.golem.interfaces.spools.keys.ParameterKey;
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
