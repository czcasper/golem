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

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import java.util.List;

/**
 * Interface describing methods required from spool which will keep information related to action definition collected by using reflection
 * on class level of objects. This spool is used to construct action stream.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 */
public interface ActionInformationSpool<A> extends AbstractSpool<A, ActionInfoKey<Class<?>>, ActionInfoProxy> {

    /**
     * Testing if given object is valid Golem action.
     *
     * @param action instance of object which will be used for testing if class of this object contains minimal set of annotation required
     *               from valid Golem action.
     * @return true in case when object represents valid Golem action, otherwise false.
     */
    public boolean isValidAction(Object action);

    /**
     * Testing if given class is valid Golem action.
     *
     * @param action instance of class which will be used for testing if it contains minimal set of annotation required from valid Golem
     *               action.
     * @return true in case when class represents valid Golem action, otherwise false.
     */
    public boolean isValidAction(Class<?> action);

    /**
     * Creating new instance of action stream from list of given objects.
     *
     * @param <V>     the type of value used in ParameterSpool.
     *
     * @param actions list of objects used to initialize action stream by actions. List must contain at least one valid action and must be
     *                different from null.
     * @return initialized instance of action stream object in case when input list is valid, otherwise null.
     */
    public <V> ActionStream<A, V> createNewFromObject(List<A> actions);

    /**
     * Creating new instance of action stream from list of given classes.
     *
     * @param <V>     the type of value used in ParameterSpool.
     *
     * @param actions list of clasess used to initialize action stream by actions. List must contain at least one valid action and must be
     *                different from null.
     * @return initialized instance of action stream object in case when input list is valid, otherwise null.
     */
    public <V> ActionStream<A, V> createNewFromClasses(List<Class<?>> actions);

    /**
     * Getting proxy information for specific instance of object or class. In case when spool doesn't contains this object proxy information
     * and object is automatically added into spool for optimizing future requests.
     *
     * @param key object or class instance which should be valid action. Must be different from null.
     * @return instance of proxy information in case when given input is valid action, otherwise null.
     */
    public ActionInfoProxy getFrom(Object key);

    /**
     * Test if spool contains specified action definition.
     *
     * @param key instance of object or class which will be used to search in spool for value.
     * @return true in case when spool contains value under requested key, otherwise false.
     */
    public boolean containsFrom(Object key);

    @Override
    public ActionInformationSpool<A> newInstance();
}
