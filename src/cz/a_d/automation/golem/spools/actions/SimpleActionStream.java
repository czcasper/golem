/*
 */
package cz.a_d.automation.golem.spools.actions;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of simple action stream based on list of actions.
 *
 * @author casper
 * @param <A> the type of action managed by stream.
 * @param <V> the type of value in parameter spool.
 */
public class SimpleActionStream<A, V> implements ActionStream<A, V> {

    /**
     * Spool of parameters connected with this stream and used to share data between actions defined in stream.
     */
    protected ParameterSpool<A, V> parmSpool;
    /**
     * List of actions stored in this stream.
     */
    protected List<A> actions;
    /**
     * Instance of iterator used to iterate actions in this stream.
     */
    protected ResetableIterator<A> it;

    /**
     * Construct action stream from given list of actions.
     *
     * @param actions collections of actions must be different from null and contains at least one valid action.
     */
    public SimpleActionStream(List<A> actions) {
        if (actions == null) {
            throw new NullPointerException("Action stream cannot be initializet by null list of actions");
        }
        if (actions.isEmpty()) {
            throw new IllegalArgumentException("Action stream cannot be initializet by empty list of actions");
        }

        if (actions instanceof AddressArrayList) {
            this.actions = actions;
        } else {
            this.actions = new AddressArrayList<>(actions);
        }
        it = new ResetableIterator<>(this.actions.iterator());
    }

    @Override
    public List<A> getActionList() {
        return actions;
    }

    @Override
    public void setParameterSpool(ParameterSpool<A, V> actionParams) {
        this.parmSpool = actionParams;
    }

    @Override
    public ParameterSpool<A, V> getParameterSpool() {
        return parmSpool;
    }

    @Override
    public ResetableIterator<A> resetableIterator() {
        return it;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        SimpleActionStream<A, V> retValue = (SimpleActionStream<A, V>) super.clone();
        retValue.it = new ResetableIterator<>(retValue.actions.iterator());
        if (parmSpool != null) {
            retValue.parmSpool = (ParameterSpool<A, V>) parmSpool.clone();
        }
        if (actions != null) {
            retValue.actions = new AddressArrayList<>(actions.size());
            for (A action : actions) {
                if (action instanceof Cloneable) {
                    try {
                        final Method cloneMethod = action.getClass().getDeclaredMethod("clone");
                        AccessController.doPrivileged(new PrivilegedAction<Object>() {
                            @Override
                            public Object run() {
                                cloneMethod.setAccessible(true);
                                return null;
                            }
                        });
                        A clone = (A) cloneMethod.invoke(action);
                        retValue.actions.add(clone);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(SimpleActionStream.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    retValue.actions.add(action);
                }
            }
        }
        return retValue;
    }
}
