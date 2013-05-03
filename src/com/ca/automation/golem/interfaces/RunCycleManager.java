/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces;

import java.util.Iterator;

/**
 *
 * @author maslu02
 */
public interface RunCycleManager<T> extends Iterator<T> {

    public boolean setup(T action, long repeatCount, int actionCount);
}
