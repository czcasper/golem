/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.keys.ParameterKey;
import com.ca.automation.golem.spools.keys.SimpleParameterKey;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public class ParameterSpoolImpl<A, V> extends AbstractSpoolImpl<A, ParameterKey<?>, V> {

    static {
        global = new ParameterSpoolImpl<Object, Object>();
    }

    public ParameterSpoolImpl() {
        super();
    }

    public ParameterSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ParameterSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    public ParameterSpoolImpl(Map<? extends ParameterKey<?>, ? extends V> m) {
        super(m);
    }

    public ParameterSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    @Override
    public AbstractSpool<A, ParameterKey<?>, V> newInstance() {
        return new ParameterSpoolImpl<A, V>();
    }

    @Override
    protected ParameterKey createKey(String key) {
        return new SimpleParameterKey(key);
    }

    @Override
    protected <P> boolean instancOfKey(P value) {
        return (value instanceof ParameterKey);
    }

    @Override
    protected ParameterKey initProxyKey() {
        return new SimpleParameterKey(null);
    }
}
