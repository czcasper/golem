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
public interface RunContext<T, V> extends Iterable<T> {

    public void setActionStream(ActionStream<T, V> stream);

    public ActionStream<T, V> getActionStream();

    public ResetableIterator<T> resetableIterator();
}
