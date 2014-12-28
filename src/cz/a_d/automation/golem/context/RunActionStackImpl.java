/*
 * 
 */
package cz.a_d.automation.golem.context;

import cz.a_d.automation.golem.common.FastStack;
import cz.a_d.automation.golem.interfaces.context.RunActionStack;
import java.util.Iterator;

/**
 * Stack of actions which can be used to run action in reverse order then action has been inserted into stack. Class is implementing basic
 * logic of stack including initialization from Stack manager. Stack can be registered by using Stack manager for action in stream.
 *
 * @author casper
 * @param <T> the type of actions stored in this action stack.
 */
public class RunActionStackImpl<T> implements RunActionStack<T>, Iterator<T>, Cloneable {

    /**
     * Stored pointer to action from action stream which is trigger for executing actions in stack.
     */
    protected T action;

    /**
     * Internal data structure used for storing actions in stack.
     */
    protected FastStack<T> actions;

    /**
     * Construct new instance of Run action stack. Initialize internal data storage for actions.
     */
    public RunActionStackImpl() {
        actions = new FastStack<>();
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

    @Override
    public boolean isEmpty() {
        return actions.isEmpty();
    }

    @Override
    public T peek() {
        return actions.peek();
    }

    @Override
    public T pop() {
        return actions.pop();
    }

    @Override
    public boolean push(T item) {
        boolean retValue = false;
        if (item != null) {
            actions.push(item);
            retValue = true;
        }
        return retValue;
    }

    @Override
    public boolean setupStack(T action, T[] actions) {
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

    @Override
    public T getAction() {
        return action;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        @SuppressWarnings("unchecked")
        RunActionStackImpl<T> retValue = (RunActionStackImpl<T>) super.clone();
        retValue.actions = new FastStack<>(actions);
        return retValue;
    }
}
