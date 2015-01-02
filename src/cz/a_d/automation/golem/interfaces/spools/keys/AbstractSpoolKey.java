/*
 */
package cz.a_d.automation.golem.interfaces.spools.keys;

/**
 * Interface describe abstract implementation of key used by spool objects.
 *
 * @author casper
 * @param <T> the type of object value used by key.
 */
public interface AbstractSpoolKey<T> extends Cloneable {

    /**
     * Changing instance of key object internally used to provide key abilities for spool.
     *
     * @param key new instance of key which will be used to provide information requested by spool. Must be different from null.
     * @return true in case when instance has been successfully changed, otherwise false.
     */
    public boolean set(T key);

    /**
     * Getter to access currently used instance of object for providing information required from key by spool.
     *
     * @return instance of object used in key implementation.
     */
    public T get();

    /**
     * Creates and returns a copy of this object.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not support the {@code Cloneable} interface. Subclasses that override
     *                                    the {@code clone} method can also throw this exception to indicate that an instance cannot be
     *                                    cloned.
     */
    public Object clone() throws CloneNotSupportedException;

    /**
     * Method provides hash code for specific instance. This allows to use this key in hash based collection.
     *
     * @return hash code representing current instance of key.
     */
    @Override
    public int hashCode();

    /**
     * Compare object with current instance. This allows to use key in collections generally.
     *
     * @param obj object which will be compared with current instance.
     * @return true in case when given object is equal to instance, otherwise false.
     */
    @Override
    public boolean equals(Object obj);

    /**
     * Getting string representation of spool key instance.
     *
     * @return string representation of current key instance.
     */
    @Override
    public String toString();

    /**
     * Loading key object from string representation.
     *
     * @param key string representation of spool key object.
     * @return true in case when loading has been successful, otherwise false.
     */
    public boolean fromString(String key);
}
