/*
 */
package com.ca.automation.golem.spools.keys;

import com.ca.automation.golem.interfaces.spools.keys.AbstractSpoolKey;

/**
 *
 * @author maslu02
 */
public abstract class AbstractSpoolKeyImpl<T> implements AbstractSpoolKey<T>, Cloneable {
    
    protected T keyValue;

    public AbstractSpoolKeyImpl(T keyValue) {
        this.keyValue = keyValue;
    }
    
    @Override
    public abstract String toString();

    @Override
    public abstract boolean fromString(String key);
    
    @Override
    public boolean set(T key) {
        if(key==null){
            return false;
        }
        keyValue = key;
        return true;
    }

    @Override
    public T get() {
        return keyValue;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public final int hashCode() {
        return (this.keyValue != null ? this.keyValue.toString().hashCode() : 0);
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractSpoolKey)) {
            return false;
        }
        final AbstractSpoolKey<?> other = (AbstractSpoolKey<?>) obj;
        
        if (this.keyValue != other.get() && (this.keyValue == null || !this.keyValue.toString().equals(other.get().toString()))) {
            return false;
        }
        return true;
    }

}