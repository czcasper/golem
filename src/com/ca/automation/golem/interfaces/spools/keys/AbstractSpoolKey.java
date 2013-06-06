/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools.keys;

/**
 *
 * @author maslu02
 */
public interface AbstractSpoolKey<T> extends Cloneable {
    
    public boolean load(String key);
    
    public boolean set(T key);
    
    public T get();
    
    public Object clone() throws CloneNotSupportedException;    
    
    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();
        
}
