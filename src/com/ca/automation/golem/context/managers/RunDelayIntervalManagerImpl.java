/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.managers;

import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.context.RunDelayIntervalImpl;
import com.ca.automation.golem.interfaces.DelayInterval;
import com.ca.automation.golem.interfaces.RunDelayIntervalManager;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public class RunDelayIntervalManagerImpl<T,K,V> extends AbstractContextManager<T, DelayInterval<T>,K,V> implements RunDelayIntervalManager<T, K, V> {

    /**
     *
     * @param context
     */
    public RunDelayIntervalManagerImpl(RunContextImpl<T,K,V> context) {
        super(context);
    }

    @Override
    public T next() {
        T retValue = null;
        if (hasNext()) {
            ListIterator<DelayInterval<T>> it = currentList.listIterator(index - 1);
            while (it.hasNext()) {
                DelayInterval<T> timer = it.next();
                retValue = timer.next();
                if (!timer.hasNext()) {
                    it.remove();
                }
            }
        }
        return retValue;
    }

    /**
     * Safelly initialize all timer members by calling one method.
     *
     * @param action = action where timer will start working
     * @param time = value greather than 0. This value used for sleeping current
     * thread is in miliseconds.
     * @param actionCount = this parmeter is used for timer live cycle
     * configuration. Must be different from zero to setup timer correctelly. In
     * case when is negative timer will live in unlimited number of iteration.
     * In case when is positive timer will live number of time stored in value.
     *
     * @return true in case when timer is initialized correctelly, otherwise
     * false.
     */
    @Override
    public boolean setup(T action, long actionCount, long time) {
        Object[] tmp = new Number[]{actionCount, time};
        return setup(action, tmp);
    }

    /**
     *
     */
    @Override
    protected void beforeNextInList() {
        current = null;
    }

    /**
     *
     */
    @Override
    protected void afterNextInList() {
        currentList.remove(--index);
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
        if (currentList == null) {
            currentList = new LinkedList<DelayInterval<T>>();
        }
        List<DelayInterval<T>> found = managed.get(action);
        if (!found.isEmpty()) {
            for (DelayInterval<T> i : found) {
                try {
                    DelayInterval<T> newDelay = (DelayInterval<T>) i.clone();
                    currentList.add(newDelay);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(RunDelayIntervalManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (current == null) {
            index = 0;
            current = currentList.get(index++);
        }
    }

    /**
     *
     * @param action
     * @param params
     * @return
     */
    @Override
    protected DelayInterval<T> setupManager(T action, Object... params) {
        DelayInterval<T> retValue = null;
        if ((context != null) && (action != null) && (params.length == 2) &&(params instanceof Number[])) {
            Number[] parm=(Number[]) params;
            DelayInterval<T> in = new RunDelayIntervalImpl<T>();
               if (in.setupTimer(action, parm[0].longValue(), parm[1].longValue())) {
                retValue = in;
            }
        }
        return retValue;
    }

    /**
     *
     * @return
     */
    @Override
    public DelayInterval<T> getCurrent() {
        return super.getCurrent();
    }

    @Override
    public List<DelayInterval<T>> getActive(){
        return currentList;
    }
    
}
