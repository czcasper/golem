/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools;

/**
 *
 * @author maslu02
 */
public interface AbstractSpoolKey extends Cloneable {
    
    public boolean load(String key);
    
    public Object clone() throws CloneNotSupportedException;    
    
    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();
        
}
