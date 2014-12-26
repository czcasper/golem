/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * @author casper
 */
public interface AbstractSpool<A, K extends AbstractSpoolKey, V> extends Map<K, V> {

    public AbstractSpool<A, K, V> newInstance();

    public V putFrom(String key, V value);

    public V getFrom(String key);

    public boolean containsFrom(String key);

    public <P> V put(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException;

    public <P> V get(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalArgumentException, IllegalAccessException;

    public Object clone();
}
