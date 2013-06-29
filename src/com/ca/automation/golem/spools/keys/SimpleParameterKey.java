/*
 */
package com.ca.automation.golem.spools.keys;

import com.ca.automation.golem.interfaces.spools.keys.ParameterKey;

/**
 *
 * @author maslu02
 */
public class SimpleParameterKey extends AbstractSpoolKeyImpl<String> implements ParameterKey<String>, Cloneable {

    public SimpleParameterKey(String key) {
        super(key);
    }

    @Override
    public boolean fromString(String key) {
        if (key == null) {
            return false;
        }
        this.keyValue = key;
        return true;
    }

    @Override
    public String toString() {
        return keyValue;
    }
}
