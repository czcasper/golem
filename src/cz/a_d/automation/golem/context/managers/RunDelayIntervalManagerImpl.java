/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.context.managers;

import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.context.RunDelayIntervalImpl;
import cz.a_d.automation.golem.interfaces.context.RunDelayInterval;
import cz.a_d.automation.golem.interfaces.context.managers.RunDelayIntervalManager;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <T>
 * @author casper
 */
public class RunDelayIntervalManagerImpl<T, C, V> extends AbstractContextManager<T, C, RunDelayInterval<T>, V> implements RunDelayIntervalManager<T, V> {

    /**
     *
     * @param context
     */
    public RunDelayIntervalManagerImpl(RunContextImpl<T, C, V> context) {
        super(context);
    }

    @Override
    public T next() {
        T retValue = null;
        if (hasNext()) {
            ListIterator<RunDelayInterval<T>> it = currentList.listIterator(index - 1);
            while (it.hasNext()) {
                RunDelayInterval<T> timer = it.next();
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
            currentList = new LinkedList<>();
        }
        List<RunDelayInterval<T>> found = managed.get(action);
        if (!found.isEmpty()) {
            for (RunDelayInterval<T> i : found) {
                try {
                    @SuppressWarnings("unchecked")
                    RunDelayInterval<T> newDelay = (RunDelayInterval<T>) i.clone();
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
    protected RunDelayInterval<T> setupManager(T action, Object... params) {
        RunDelayInterval<T> retValue = null;
        if ((context != null) && (action != null) && (params.length == 2) && (params instanceof Number[])) {
            Number[] parm = (Number[]) params;
            RunDelayInterval<T> in = new RunDelayIntervalImpl<>();
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
    public RunDelayInterval<T> getCurrent() {
        return super.getCurrent();
    }

    @Override
    public List<RunDelayInterval<T>> getActive() {
        return currentList;
    }
}
