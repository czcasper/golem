/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Interface describe methods required from spool of values used by Golem to provide access to information stored in spool. This list of
 * method declared in interface is used to provide basic Golem features related to injection of values.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 * @param <K> the type of key value in spool.
 * @param <V> the type of value in spool.
 */
public interface AbstractSpool<A, K extends AbstractSpoolKey, V> extends Map<K, V> {

    /**
     * Creating new empty instance of spool.
     *
     * @return new instance of spool.
     */
    public AbstractSpool<A, K, V> newInstance();

    /**
     * Putting value into spool by using string representation of key. Before putting value is string wrapped into object which is currently
     * used like key in spool.
     *
     * @param key   instance of string which will be used like key for value.
     * @param value instance of object which will be stored like value under key into spool.
     *
     * @return true in case when value putting value into spool has been successful, otherwise false.
     */
    public V putFrom(String key, V value);

    /**
     * Getting value from stream based on string representation of key. Simplification for accessing values in spool based on value stored
     * in annotation of field.
     *
     * @param key
     * @return
     */
    public V getFrom(String key);

    /**
     * Testing if spool is containing key represented by string taken from method parameter.
     *
     * @param key string value which will be converted into key used by spool and used for searching value under this key in spool.
     *
     * @return instance of object stored in spool if there is some object under requested key, otherwise null.
     */
    public boolean containsFrom(String key);

    /**
     * Putting value into spool based on key created from information defined in action field.
     *
     * @param <P>        the type of value used in ParameterSpool.
     *
     * @param action     instance of action used to get value and values from Golem defined annotation to create key for value. Must be
     *                   different from null.
     * @param f          field defined by class of action. Value of this field will be stored in spool.
     * @param parameters spool of parameter used by current run to provide ability of spools react on pointer feature provided by parameter
     *                   annotation.
     * @return null in case when there was no object under defined key, otherwise instance of value previously stored in spool.
     *
     * @throws IllegalAccessException in case when spool doesn't supports annotation type specified on field.
     */
    public <P> V put(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalAccessException;

    /**
     * Getting value from spool based on key generated from values defined in Golem annotation and injecting this value into action field.
     *
     * @param <P>        the type of value used in ParameterSpool.
     *
     * @param action     instance of action used to get values stored on field level defined by Golem annotations to generate key for
     *                   retrieving value from spool and injecting into this object into field. Must be different from null.
     * @param f          field defined by class of action. Value of this field contains value stored in spool.
     * @param parameters spool of parameter used by current run to provide ability of spools react on pointer feature provided by parameter
     *                   annotation.
     * @return previously stored value in field of action object before injecting value from spool.
     *
     * @throws IllegalAccessException in case when spool doesn't supports annotation type specified on field.
     */
    public <P> V get(A action, Field f, ParameterSpool<A, P> parameters) throws IllegalAccessException;

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
