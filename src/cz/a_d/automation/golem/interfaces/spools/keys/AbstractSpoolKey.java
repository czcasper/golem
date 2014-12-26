/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.spools.keys;

/**
 *
 * @author casper
 */
public interface AbstractSpoolKey<T> extends Cloneable {
    
    
    public boolean set(T key);
    
    public T get();
    
    public Object clone() throws CloneNotSupportedException;    
    
    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();
        
    public boolean fromString(String key);
}
