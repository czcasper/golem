/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces;

import cz.a_d.automation.golem.common.iterators.ResetableIterator;

/**
 *
 * @author casper
 */
public interface RunContext<T, V> extends Iterable<T> {

    public void setActionStream(ActionStream<T, V> stream);

    public ActionStream<T, V> getActionStream();

    public ResetableIterator<T> resetableIterator();
}
