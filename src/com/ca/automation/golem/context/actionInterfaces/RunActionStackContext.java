/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.actionInterfaces;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public interface RunActionStackContext<T> {

    /**
     *
     * @return
     */
    boolean isEmpty();

    /**
     *
     * @return
     */
    T getAction();

    /**
     *
     * @return
     */
    T peek();

    /**
     *
     * @return
     */
    T pop();

    /**
     *
     * @param item
     * @return
     */
    boolean push(T item);
    
}
