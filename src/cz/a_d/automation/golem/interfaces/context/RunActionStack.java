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

/**
 * Interface describing feature of stack with actions which can be used to run action in reverse order then action has been inserted into
 * stack. Implementation needs to have just basic logic of stack including initialization from Stack manager. Stack can be registered by
 * using Stack manager for action in stream.
 *
 * @author casper
 * @param <T> the type of actions managed by stack manager.
 */
public interface RunActionStack<T> extends CloneableIterator<T> {

    /**
     * This method safely initialize stack by name idendificator and collections of actions.
     *
     * @param action
     * @param actions Non empty collection with actions
     *
     * @return true if stack is correctly initialized, otherwise false.
     */
    public boolean setupStack(T action, T[] actions);

    /**
     * Get action where stack is defined to start.
     *
     * @return stack start action object
     */
    public T getAction();

    /**
     * This method push action into stack.
     *
     * @param item action which shlould be push into stack
     * @return bool in case when success, otherwise false
     */
    public boolean push(T item);

    /**
     * Method return and remove first object in stack.
     *
     * @return first object in stack before method call.
     */
    public T pop();

    /**
     * Method allows access to object on top of stack, object stay in stack.
     *
     * @return Object on the top of stack
     */
    public T peek();

    /**
     * Testing if action stack is empty.
     *
     * @return true in case when there is no item in stack, otherwise false.
     */
    public boolean isEmpty();
}
