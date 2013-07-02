/*
 */
package cz.a_d.automation.golem.context.managers;

import cz.a_d.automation.golem.context.RunActionStackImpl;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.interfaces.context.RunActionStack;
import cz.a_d.automation.golem.interfaces.context.managers.RunActionStackManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <T>
 * @author maslu02
 */
public class RunActionStackManagerImpl<T, C, V> extends AbstractContextManager<T, C, RunActionStack<T>, V> implements RunActionStackManager<T, V> {

    /**
     * Cosntructor fot this type of manager.
     *
     * @param context this context must refered to RunnerContext implementation
     */
    public RunActionStackManagerImpl(RunContextImpl<T, C, V> context) {
        super(context);
    }

    @Override
    public List<RunActionStack<T>> getActive() {
        return currentList;
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
    @SuppressWarnings("unchecked")
    @Override
    protected void afterNextInList() {
        try {
            current = (RunActionStack<T>) current.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RunActionStackManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
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
    @SuppressWarnings("unchecked")
    @Override
    protected void loadManger(T action) {
        if (current == null) {
            currentList = managed.get(action);
            index = 0;
            try {

                current = (RunActionStack<T>) currentList.get(index++).clone();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RunActionStackManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
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
            RunActionStack<T> tmp = new RunActionStackImpl<>();
            if (tmp.setupStack(action, (T[]) params)) {
                retValue = tmp;
            }
        }
        return retValue;
    }
}
