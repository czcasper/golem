/*
 */
package cz.a_d.automation.golem.context.managers;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.interfaces.context.managers.ContextManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class describe functionality of general context manager and provide 
 * solution for common manager problems. Support ability to have assigned more 
 * than one context manager on single action. Provide 
 * 
 * 
 * @author casper
 * 
 * @param <T> - type of Action 
 * @param <C> - type of object used for validation of run results
 * @param <M> - type of object managed by manager. It must be child of Iterator
 * @param <V> - type of object used by parameter map like value
 */
public abstract class AbstractContextManager<T, C extends Object, M extends Iterator<T>, V> implements ContextManager<T, M, V> {

    /**
     *
     */
    protected Map<T, List<M>> managed;
    /**
     *
     */
    protected List<M> currentList;
    /**
     *
     */
    protected M current;
    /**
     *
     */
    protected int index;
    /**
     *
     */
    protected RunContextImpl<T, C, V> context;

    /**
     *
     * @param context
     */
    public AbstractContextManager(RunContextImpl<T, C, V> context) {
        this.context = context;
        managed = new HashMap<>();
    }

    /**
     *
     * @param action
     * @param parms
     * @return
     */
    @Override
    public boolean setup(T action, Object... parms) {
        boolean retValue = false;
        M man = setupManager(action, parms);
        if (man != null) {
            List<M> tmpList;
            if (managed.containsKey(action)) {
                tmpList = managed.get(action);
            } else {
                tmpList = new AddressArrayList<>();
                managed.put(action, tmpList);
            }
            tmpList.add(man);
            retValue = true;
        }
        return retValue;
    }

    /**
     *
     * @param action
     */
    @Override
    public void load(T action) {
        if (managed.containsKey(action)) {
            loadManger(action);
        }
    }

    @Override
    public boolean hasNext() {
        boolean retValue = false;
        if (current != null) {
            do {
                while ((retValue = current.hasNext()) || (index < currentList.size())) {
                    if (retValue) {
                        break;
                    }

                    beforeNextInList();

                    current = currentList.get(index++);

                    afterNextInList();
                }
                if ((!retValue) && (!currentFinilizer())) {
                    current = null;
                }

            } while ((!retValue) && (current != null));
        }
        return retValue;
    }

    @Override
    public T next() {
        T retValue = null;
        if (hasNext()) {
            retValue = current.next();
        }
        return retValue;
    }

    @Override
    public void remove() {
        if (current != null) {
            current.remove();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public M getCurrent() {
        return current;
    }

    /**
     *
     */
    protected abstract void beforeNextInList();

    /**
     *
     */
    protected abstract void afterNextInList();

    /**
     *
     * @return
     */
    protected abstract boolean currentFinilizer();

    /**
     *
     * @param action
     */
    protected abstract void loadManger(T action);

    /**
     *
     * @param action
     * @param params
     * @return
     */
    protected abstract M setupManager(T action, Object... params);
}
