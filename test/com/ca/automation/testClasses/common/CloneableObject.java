/*
 */
package com.ca.automation.testClasses.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author maslu02
 */
// TODO Documentation: Create javadoc on Class and public methods.
public class CloneableObject<T> implements Cloneable {

    private T value;

    public CloneableObject() {
    }

    public CloneableObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        CloneableObject<T> retValue = (CloneableObject<T>) super.clone();
        try {
            Class<? extends Object> vClass = value.getClass();
            Constructor<? extends Object> constructor = vClass.getConstructor(vClass);
            retValue.value = (T) constructor.newInstance(value);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            throw new CloneNotSupportedException(ex.getLocalizedMessage());
        }
        return retValue;
    }
}
