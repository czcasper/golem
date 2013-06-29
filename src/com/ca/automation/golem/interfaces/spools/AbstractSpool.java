/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools;

import com.ca.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public interface AbstractSpool<A, K extends AbstractSpoolKey, V> extends Map<K, V> {

    public AbstractSpool<A, K, V> newInstance();

    public V put(String key, V value);

    public V get(String key);

    public boolean contains(String key);

    public <P> V put(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException;

    public <P> V get(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException;

    public Object clone();
}
