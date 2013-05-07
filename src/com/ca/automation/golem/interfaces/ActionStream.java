/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

import com.ca.automation.golem.common.iterators.ResetableIterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public interface ActionStream<T, K, V> extends Cloneable {

    public List<T> getActionList();

    public void setParameter(Map<K, V> actionParams);

    public Map<K, V> getParameterMap();

    public ResetableIterator<T> resetableIterator();

    public Object clone() throws CloneNotSupportedException;
}
