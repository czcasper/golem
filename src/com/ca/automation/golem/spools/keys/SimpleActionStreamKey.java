/*
 */
package com.ca.automation.golem.spools.keys;

import com.ca.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 *
 * @author maslu02
 */
public class SimpleActionStreamKey implements ActionStreamKey<String>, Cloneable {

    protected String key;

    public SimpleActionStreamKey(String key) {
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
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.key != null ? this.key.hashCode() : 0);
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
        final SimpleActionStreamKey other = (SimpleActionStreamKey) obj;
        if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean fromString(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
