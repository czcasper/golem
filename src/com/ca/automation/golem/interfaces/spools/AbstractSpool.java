/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools;

import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public interface AbstractSpool<A,K extends AbstractSpoolKey,V> extends Map<K,V> {
    
    public V put(String key,V value);
    public <P> V put(A action, Field f,AbstractSpool<A,ParameterKey,P> parameters,V value);
    public boolean contains(String key);
    
    public V get(String key);
    public <P> V get(A action, Field f,AbstractSpool<A,ParameterKey,P> parameters);
    public Object clone();
    
}
