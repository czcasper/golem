/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

import com.ca.automation.golem.common.iterators.ResetableIterator;

/**
 *
 * @author maslu02
 */
public interface RunContext<T,K,V> extends Iterable<T> {
    
    public void setActionStream(ActionStream<T,K,V> stream);
    public ActionStream<T,K,V> getActionStream();

    public ResetableIterator<T> resetableIterator();
    
}
