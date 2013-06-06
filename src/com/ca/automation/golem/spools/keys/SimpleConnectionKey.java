/*
 */
package com.ca.automation.golem.spools.keys;

import com.ca.automation.golem.interfaces.spools.keys.ConnectionKey;

/**
 *
 * @author maslu02
 */
public class SimpleConnectionKey implements ConnectionKey<String>, Cloneable {

    protected String key;

    public SimpleConnectionKey(String key) {
        this.key = key;
    }

    @Override
    public boolean set(String key) {
        boolean retValue = false;
        if ((key != null) && (!key.isEmpty())) {
            this.key = key;
            retValue = true;
        }
        return retValue;
    }

    @Override
    public String get() {
        return key;
    }

    @Override
    public boolean load(String key) {
        if (key == null) {
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
        hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
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
        final SimpleConnectionKey other = (SimpleConnectionKey) obj;
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
