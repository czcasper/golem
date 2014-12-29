/*
 * 
 */
package cz.a_d.automation.golem.context;

import cz.a_d.automation.golem.interfaces.context.RunDelayInterval;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Golem implementation of interface RunDelayInterval. Delay is triggered by specified action from action stream. Waiting can be defined for
 * fixed number of actions of for unlimited number of them.
 *
 * @author casper
 * @param <T> the type of actions managed by delay manager.
 */
public class RunDelayIntervalImpl<T> implements RunDelayInterval<T>, Cloneable {

    /**
     * Amount of time which is used to waiting before next step in action is processed. Time is in milliseconds.
     */
    protected long time = 0;

    /**
     * Count of action for which is wait feature active. In case when is negative integer is delay feature active to unlimited time, in case
     * of positive integer number is delay active just for specified amount of methods. Zero value means wait feature is not active.
     */
    protected long actionCount = 0;

    /**
     * Index of currently processed action. First triggered action has index zero.
     */
    protected long iterator = 0;

    /**
     * Instance of action from action stream which is triggering delay feature.
     */
    protected T action;

    @Override
    public boolean hasNext() {
        boolean retValue = false;
        if ((time != 0) && (actionCount != 0) && (actionCount != iterator)) {
            if ((actionCount < 0) || ((iterator < actionCount))) {
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public T next() {
        T retValue = null;
        if (hasNext()) {
            iterator++;
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(RunDelayIntervalImpl.class.getName()).log(Level.SEVERE, "Waiting process was interupted by external source. For current action:" + action.toString(), ex);
            }
            retValue = action;
        }
        return retValue;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setupTimer(T action, long actionCount, long time) {
        boolean retValue = false;
        if ((actionCount != 0) && (setTime(time))) {
            this.action = action;
            this.actionCount = actionCount;
            this.iterator = 0;
            retValue = true;
        }
        return retValue;
    }

    @Override
    public T getAction() {
        return action;
    }

    @Override
    public long getActionCount() {
        return iterator;
    }

    @Override
    public void setActionCount(long actionCount) {
        this.actionCount = actionCount;
    }

    @Override
    public void stop() {
        iterator = actionCount;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public boolean setTime(long time) {
        boolean retValue = false;
        if (time > 0) {
            this.time = time;
            retValue = true;
        }
        return retValue;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
