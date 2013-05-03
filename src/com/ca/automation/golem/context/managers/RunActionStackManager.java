/*
 */
package com.ca.automation.golem.context.managers;

import com.ca.automation.golem.context.RunActionStack;
import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.context.actionInterfaces.managers.RunActionStackManagerContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public class RunActionStackManager<T,K,V> extends AbstractContextManager<T, RunActionStack<T>,K,V> implements RunActionStackManagerContext<T> {

    /**
     * Cosntructor fot this type of manager.
     *
     * @param context this context must refered to RunnerContext implementation
     */
    public RunActionStackManager(RunContextImpl<T,K,V> context) {
        super(context);
    }

    /**
     *
     */
    @Override
    protected void beforeNextInList() {
    }

    /**
     *
     */
    @Override
    protected void afterNextInList() {
        try {
            current = current.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RunActionStackManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    @Override
    protected boolean currentFinilizer() {
        return false;
    }

    /**
     *
     * @param action
     */
    @Override
    protected void loadManger(T action) {
        if (current == null) {
            currentList = managed.get(action);
            index = 0;
            try {
                current = currentList.get(index++).clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RunActionStackManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /*
     * This method safely initialize stack in runner manager under the action 
     * object by name idendificator and collection of actions.
     * 
     * @param action key action used for starting poping actions from stack
     *
     */
    /**
     *
     * @param action
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    protected RunActionStack<T> setupManager(T action, Object... params) {
        RunActionStack<T> retValue = null;
        if ((action != null) && (params != null) && (params.length > 0)) {
            RunActionStack<T> tmp = new RunActionStack<T>();
            if (tmp.setupStack(action, (T[]) params)) {
                retValue = tmp;
            }
        }
        return retValue;
    }
}
