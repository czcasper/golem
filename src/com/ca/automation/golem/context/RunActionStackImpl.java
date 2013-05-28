/*
 * 
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.common.FastStack;
import com.ca.automation.golem.interfaces.context.RunActionStack;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public class RunActionStackImpl<T> implements RunActionStack<T>, Iterator<T>, Cloneable {

    /**
     *
     */
    protected T action;
    /**
     *
     */
    protected LinkedList<T> actions;

    /**
     *
     */
    public RunActionStackImpl() {
        actions = new FastStack<T>();
    }

    @Override
    public boolean hasNext() {
        if (actions == null) {
            return false;
        }
        return !actions.isEmpty();
    }

    @Override
    public T next() {
        T retValue = null;
        if (hasNext()) {
            retValue = actions.pop();
        }
        return retValue;
    }

    @Override
    public void remove() {
        if ((action != null) && (!actions.isEmpty())) {
            actions.pop();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return actions.isEmpty();
    }

    /**
     *
     * @return
     */
    @Override
    public T peek() {
        return actions.peek();
    }

    /**
     *
     * @return
     */
    @Override
    public T pop() {
        return actions.pop();
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean push(T item) {
        boolean retValue = false;
        if (item != null) {
            actions.push(item);
            retValue = true;
        }
        return retValue;
    }

    /**
     * This method safely initialize stack by name idendificator and collections
     * of actions.
     *
     * @param action 
     * @param actions Non empty collection with actions
     *
     * @return true if stack is correctly initialized, otherwise false.
     */
    @Override
    public boolean setupStack(T action, T... actions) {
        boolean retValue = false;
        if ((this.actions != null) && (actions != null) && (actions.length > 0)) {
            for (T o : actions) {
                this.actions.push(o);
            }
            this.action = action;
            retValue = true;
        }
        return retValue;
    }

    /**
     * Get action where stack is defined to start.
     *
     * @return stack start action object
     */
    @Override
    public T getAction() {
        return action;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() throws CloneNotSupportedException {
        RunActionStackImpl<T> retValue = (RunActionStackImpl<T>) super.clone();
        retValue.actions = new LinkedList<T>(actions);
        return retValue;
    }
}
