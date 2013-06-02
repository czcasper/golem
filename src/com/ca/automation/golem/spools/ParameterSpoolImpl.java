/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.spools.ParameterKey;
import com.ca.automation.golem.spools.keys.SimpleParameterKey;

/**
 *
 * @author maslu02
 */
public class ParameterSpoolImpl<A,V> extends AbstractSpoolImpl<A, ParameterKey, V> {

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
