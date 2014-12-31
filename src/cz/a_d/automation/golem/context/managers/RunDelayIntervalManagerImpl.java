/*
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
 * Implementation of interface specific to manager of delay feature.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 * @param <C> the type of object used for validation of run results
 */
public class RunDelayIntervalManagerImpl<T, C, V> extends AbstractContextManager<T, C, RunDelayInterval<T>, V> implements RunDelayIntervalManager<T, V> {

    /**
     * Creating instance of manager of delay interval.
     *
     * @param context run context for which is this manager implementation registered. Must be different from null.
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

    @Override
    public boolean setup(T action, long actionCount, long time) {
        Object[] tmp = new Number[]{actionCount, time};
        return setup(action, tmp);
    }

    @Override
    protected void beforeNextInList() {
        current = null;
    }

    @Override
    protected void afterNextInList() {
        currentList.remove(--index);
    }

    @Override
    protected boolean currentFinilizer() {
        return false;
    }

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

    @Override
    public RunDelayInterval<T> getCurrent() {
        return super.getCurrent();
    }

    @Override
    public List<RunDelayInterval<T>> getActive() {
        return currentList;
    }
}
