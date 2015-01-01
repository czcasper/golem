/*
 */
package cz.a_d.automation.golem.interfaces.context;

import java.util.Iterator;

/**
 * Interface describe base requirements to Golem action stream feature. Feature must implement this interface to allow cloning of feature
 * and reusing registered configuration during stream processing.
 *
 * @author casper
 * @param <T> the type of action managed by feature implementation.
 */
public interface CloneableIterator<T> extends Iterator<T> {

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not support the {@code Cloneable} interface. Subclasses that override
     *                                    the {@code clone} method can also throw this exception to indicate that an instance cannot be
     *                                    cloned.
     */
    public Object clone() throws CloneNotSupportedException;
}
