/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

import java.util.Iterator;

/**
 * This is base interface for all classes which are dealing directly with actions in stream.
 * All classes which implements this interface or childs of this interface must also implement
 * interface Clonable. Otherwise CloneNotSupportedException exception will be throwed.
 * 
 * @author maslu02
 */
public interface CloneableIterator<T> extends Iterator<T> {
    public Object clone() throws CloneNotSupportedException;
}
