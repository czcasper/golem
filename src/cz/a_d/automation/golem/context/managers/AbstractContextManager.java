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
package cz.a_d.automation.golem.context.managers;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.interfaces.context.managers.ContextManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of interface abstraction of common tasks solved by all context managers. All functions requested from content manager are
 * defined in this implementation.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <M> the type of manged feature.
 * @param <V> the type of value used in parameter spool.
 * @param <C> the type of object used for validation of run results
 */
public abstract class AbstractContextManager<T, C extends Object, M extends Iterator<T>, V> implements ContextManager<T, M, V> {

    /**
     * Storage for all implementation of feature managed by current manager. Key is instance of action from action stream which will be used
     * for triggering feature implementation. Value is list of registered instances for this specific action.
     */
    protected Map<T, List<M>> managed;

    /**
     * List of currently active instance of managed feature. This list is used by manager to collect all active instances and realize logic
     * of managed feature.
     */
    protected List<M> currentList;

    /**
     * Currently active instance of managed feature.
     */
    protected M current;

    /**
     * Index of currently active instance of managed feature in list of active features.
     */
    protected int index;

    /**
     * Instance of run context where manager is active and providing features. This allows to share data stored in run context with manager
     * and feature implementation.
     */
    protected RunContextImpl<T, C, V> context;

    /**
     * Creating instance of manager, initialize context and map for storing all defined managed feature.
     *
     * @param context run context for which is this manager implementation registered. Must be different from null.
     */
    public AbstractContextManager(RunContextImpl<T, C, V> context) {
        this.context = context;
        managed = new HashMap<>();
    }

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

    @Override
    public M getCurrent() {
        return current;
    }

    /**
     * Method is called before next item from active list is retrieved. This allows to make necessary clean up actions before feature is
     * removed from processing.
     */
    protected abstract void beforeNextInList();

    /**
     * Method is called after next item from active list is retrieved. This allows to make necessary initialization before feature is
     * implementation is first time used.
     */
    protected abstract void afterNextInList();

    /**
     * Method is called when current feature iterator doesn't have next item and feature manager can validate if there is other active
     * instance of managed feature implementation.
     *
     * @return true in case when manager has active managed feature, otherwise false.
     */
    protected abstract boolean currentFinilizer();

    /**
     * Method is called in case when there is registered instance of managed feature in storage. Method must implement logic for adding
     * registered instances into processing.
     *
     * @param action instance of action which is triggering loading of managed features registered managed feature connected with this
     *               action.
     */
    protected abstract void loadManger(T action);

    /**
     * Registering and configuring managed feature handled by this manager implementation. Method allows to initialize specific feature in
     * concrete implementation of manager.
     *
     * @param action instance used to trigger feature configured by manager.
     * @param params parameters used by manager to configure managed feature.
     * 
     * @return true in case when new instance of managed feature is configured properly, otherwise return false.
     */
    protected abstract M setupManager(T action, Object... params);
}
