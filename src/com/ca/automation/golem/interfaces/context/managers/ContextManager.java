/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.context.managers;

import java.util.Iterator;

/**
 *
 * @author maslu02
 */
public interface ContextManager<T, M extends Iterator<T>, V> extends Iterator<T> {

    public boolean setup(T action, Object... parms);

    public void load(T action);

    public M getCurrent();
}
