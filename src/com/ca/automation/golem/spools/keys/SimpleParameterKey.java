/*
 */
package com.ca.automation.golem.spools.keys;

import com.ca.automation.golem.interfaces.spools.ParameterKey;

/**
 *
 * @author maslu02
 */
public class SimpleParameterKey implements ParameterKey {

    protected String key;

    public SimpleParameterKey(String key) {
        this.key = key;
    }

    @Override
    public boolean load(String key) {
        if(key ==null){
            return false;
        }
        this.key = key;
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleParameterKey other = (SimpleParameterKey) obj;
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return key;
    }
}
