/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.AbstractSpoolKey;

/**
 * Implementation of abstract key used by Golem spools to index values.
 *
 * @author casper
 * @param <T> the type of action managed by spool.
 */
public abstract class AbstractSpoolKeyImpl<T> implements AbstractSpoolKey<T>, Cloneable {

    /**
     * Storage for object represents key value.
     */
    protected T keyValue;

    /**
     * Construct new instance of key from given object.
     *
     * @param keyValue instance of object used to provide information about key.
     */
    public AbstractSpoolKeyImpl(T keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean fromString(String key);

    @Override
    public boolean set(T key) {
        if (key == null) {
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
        return (this.keyValue != null ? this.toString().hashCode() : 0);
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

        return !(this.keyValue != other.get() && (this.keyValue == null || !this.toString().equals(other.toString())));
    }
}
