/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.context;

/**
 *
 * @author maslu02
 */
public interface RunActionStack<T> extends CloneableIterator<T> {

    /**
     * This method safely initialize stack by name idendificator and collections
     * of actions.
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
     *
     * @return
     */
    public T pop();

    /**
     *
     * @return
     */
    public T peek();

    /**
     *
     * @return
     */
    public boolean isEmpty();
}
