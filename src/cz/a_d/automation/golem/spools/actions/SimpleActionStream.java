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
 *
 * @author casper
 */
// TODO Documentation: Create Javadoc on class and public method level.
public class SimpleActionStream<A, V> implements ActionStream<A, V> {

    protected ParameterSpool<A, V> parmSpool;
    protected List<A> actions;
    protected ResetableIterator<A> it;

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
