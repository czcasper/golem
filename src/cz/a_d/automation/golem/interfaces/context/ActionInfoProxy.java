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
package cz.a_d.automation.golem.interfaces.context;

import cz.a_d.automation.golem.interfaces.spools.AbstractSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.enums.ActionMethodProxyType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Interface describe wrapped information collected by reflection from action class to format requested by Golem runner. This proxy
 * information allows to minimize scanning of class by using reflection, also simplify implementation of particular runner features.
 *
 * @author casper
 */
public interface ActionInfoProxy {

    /**
     * Appending of mapped proxy object into this instance. This method is solving action inheritance implementation.
     *
     * @param other instance of proxy object which contains informations about parent class.Must be different from null.
     * @return true in case when informations are successfully added into current instance. Otherwise false.
     */
    public boolean addActionInfoProxy(ActionInfoProxy other);

    /**
     * Adding action field with specified field type into proxy object.
     *
     * @param type   specify type of field added into proxy object. Must be different from null.
     * @param fields collection of field which will be added into proxy object with defined type. Must be different from null and contains
     *               at least one valid instance of Field.
     * @return true in case when information has been added successfully, otherwise false.
     */
    public boolean addField(ActionFieldProxyType type, Collection<Field> fields);

    /**
     * Adding action method with specified method type into proxy object.
     *
     * @param type    specify type of method added into proxy object. Must be different from null.
     * @param methods collection of action methods which will be added into proxy object with defined type. Must be different from null and
     *                contains at least one valid instance of Method.
     * @return true in case when information has been added successfully, otherwise false.
     */
    public boolean addMethod(ActionMethodProxyType type, Collection<Method> methods);

    /**
     * Getter to allow access to all field with defined type registered by current instance of proxy object.
     *
     * @param type type of field returned from proxy object. Must be different from null.
     * @return list of field registered by current instance. Null in case when there is nothing registered under this requested type or
     *         requested type is null.
     */
    public List<Field> getField(ActionFieldProxyType type);

    /**
     * Getter to access action field by name.
     *
     * @param name action field name, must be non empty and different from null.
     * @return instance of Field object pointing to action field defined by Golem annotation. Null in case when there is no field with this
     *         name registered by current proxy instance.
     */
    public Field getField(String name);

    /**
     * Getter to access action method registered by this proxy instance.
     *
     * @param type type of method returned from proxy object. Must be different from null.
     * @return sorted collection of method in order which must be used for action method processing, in case when there is registered at
     *         least one method with requested type in proxy object, otherwise null.
     */
    public SortedSet<Method> getMethod(ActionMethodProxyType type);

    /**
     * Getter to access all types of action fields mapped in proxy.
     *
     * @return map of fields registered in current instance of proxy. Never return null.
     */
    public Map<ActionFieldProxyType, List<Field>> getFields();

    /**
     * Getter to access all types of action methods mapped in proxy.
     *
     * @return map of methods registered in current instance of proxy. Never return null.
     */
    public Map<ActionMethodProxyType, SortedSet<Method>> getMethods();

    /**
     * Getter to access map of all action fields registered by current instance.
     *
     * @return map of action field stored under key based on action field name.
     */
    public Map<String, Field> getFieldNames();

    /**
     * Creating new instance of proxy object from given class and storing into spool of proxy objects. This method is save way how to put
     * action defined by java class into spool of proxy objects.
     *
     * @param <A>    the type of action managed by spool.
     * @param action class which should be loaded into proxy object. Class needs to follow convection required by Golem.
     * @param loaded instance of spool object which storing proxy objects. Must be different from null.
     * @return true in case when class is valid action and has been successfully added into spool, otherwise false.
     */
    public <A> boolean loadAction(Class<?> action, AbstractSpool<A, ActionInfoKey<Class<?>>, ActionInfoProxy> loaded);

    /**
     * Testing if proxy object is empty. No registered field or methods inside proxy.
     *
     * @return true in case when instance of proxy object is empty, otherwise false.
     */
    public boolean isEmpty();

    /**
     * Testing if proxy object contains valid definition of Golem action object. This method testing if action contains all mandatory
     * information required by Golem runner.
     *
     * @return true in case when proxy contains valid representation of Golem action, otherwise false.
     */
    public boolean isValid();

    /**
     * Clearing all information stored in current instance of proxy object.
     */
    public void clear();

    /**
     * Generating valid hash code for current proxy instance. This method must be implemented to provide ability store proxy object in
     * collection based on hash values.
     *
     * @return
     */
    @Override
    public int hashCode();

    /**
     * Comparing current instance with object in parameter. This methods need to be implemented to allow usage of proxy object generally in
     * collections.
     *
     * @param obj instance of object which should be compared with current instance.
     * @return true in case when object given by parameter is equal to current instance.
     */
    @Override
    public boolean equals(Object obj);
}
