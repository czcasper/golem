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
package cz.a_d.automation.golem.interfaces;

import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import java.util.Iterator;

/**
 * Interface describing all methods required by Golem runner. This methods are used during action stream processing and execution of action
 * methods.
 *
 * @author casper
 * @param <A> the type of actions managed by this steam.
 * @param <V> the type of value used in parameter spool.
 */
public interface ActionStream<A, V> extends Cloneable {

    /**
     * Testing whether action stream contains some action.
     *
     * @return true in case when action stream contains actions, otherwise false.
     */
    public boolean isEmpty();

    /**
     * Changing spool of parameters used by action stream.
     *
     * @param actionParams instance of parameter spool. Must be different from null.
     */
    public void setParameterSpool(ParameterSpool<A, V> actionParams);

    /**
     * Getter to access currently used instance of parameter spool.
     *
     * @return instance of parameter spool. Never return null value.
     */
    public ParameterSpool<A, V> getParameterSpool();

    /**
     * Getter to access instance of iterator used for iteration actions in stream.
     *
     * @return instance of iterator which is providing access to actions in stream. Never return null value.
     */
    public ResetableIterator<A> resetableIterator();

    /**
     * Creates deep copy of action stream, this copy contains all content of parameters and sub set of actions stored between actions
     * defined by parameters including this two actions.
     *
     * @param startAction action which defines beginning of new sub stream.
     * @param endAction   action which defines end of new sub stream.
     * @return instance of action stream in case when parameters are defining valid sequence of actions, otherwise null.
     */
    public ActionStream<A, V> subStream(A startAction, A endAction);

    /**
     * Testing whether specific instance of action is stored in action stream.
     *
     * @param action instance of action which will be tested if is presented in action stream.
     * @return true in case when instance of action is in action stream, otherwise false.
     */
    public boolean contains(A action);

    /**
     * Testing if first action from parameter is stored in action stream before action defined by second parameter including if start and
     * stop action has same index.
     *
     * @param action    tested action, must be different from null and alredy stored in action stream.
     * @param endAction action used like end marker for test operation. Must be different from null and already stored in action stream.
     * @return true in case when tested action is before end marker or is end marker and parameters are valid, otherwise false.
     */
    public boolean isBefore(A action, A endAction);

    /**
     * Getter to access action based on position of action and vicinity defined by index.
     *
     * @param action instance of action which will define start position in action stream. Must be different from null.
     * @param index  could be positive or negative number. Value of index summed with position defined by action and must be positive number
     *               less then amount of actions stored inside action stream.
     * @return instance of action in case when parameters are valid, otherwise null.
     */
    public A getAction(A action, int index);

    /**
     * Getter to access iterator which iterate through actions in stream and start on action defined be input parameter.
     *
     * @param action valid action stored in action stream.
     * @return instance of iterator in case when parameter is valid, otherwise null.
     */
    public Iterator<A> iterator(A action);

    /**
     * Clear all actions stored in action stream. Result of this method is action stream without actions, parameters are not changed.
     */
    public void clear();

    /**
     * Supports cloning of action stream. This allows duplicate action streams and run it in parallel.
     *
     * @return deep copy of action stream including all actions and parameters stored inside stream.
     * @throws CloneNotSupportedException if some object stored in action stream does not supports cloning.
     */
    public Object clone() throws CloneNotSupportedException;
}
