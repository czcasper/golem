/*
 */
package com.ca.automation.golem.spools.actions;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.spools.ActionInformationSpool;
import com.ca.automation.golem.interfaces.spools.ParameterSpool;
import com.ca.automation.golem.spools.ActionInformationSpoolImpl;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
// TODO Documentation: Create Javadoc on class and public method level.
public class SimpleActionStream<A,V> implements ActionStream<A, V> {

    protected ParameterSpool<A ,V> parmSpool;
    protected List<A> actions;
    protected ResetableIterator<A> it;

    public SimpleActionStream(List<A> actions) {
        if (actions == null) {
            throw new NullPointerException("Action stream cannot be initializet by null array of actions");
        }
        this.actions = new AddressArrayList<>(actions.size());
        ActionInformationSpool<Object> spool = ActionInformationSpoolImpl.getGlobal();
        for (A action : actions) {
            if (spool.isValidAction(action)) {
                this.actions.add(action);
            } else {
                Logger.getLogger(SimpleActionStream.class.getName()).log(Level.INFO, "Action {0} is not valid runner action.", action.toString());
            }
        }
        if (this.actions.isEmpty()) {
            throw new NullPointerException("List doesnt contains valid actions");
        }
        it = new ResetableIterator<>(this.actions.iterator());
    }

    @Override
    public List<A> getActionList() {
        return actions;
    }

    @Override
    public void setParameter(ParameterSpool<A ,V> actionParams) {
        this.parmSpool = actionParams;
    }

    @Override
    public ParameterSpool<A ,V> getParameterMap() {
        return parmSpool;
    }

    @Override
    public ResetableIterator<A> resetableIterator() {
        return it;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
        SimpleActionStream<A,V> retValue = (SimpleActionStream<A,V>) super.clone();
        retValue.it = new ResetableIterator<>(retValue.actions.iterator());
        if (parmSpool != null) {
            
            retValue.parmSpool =  (ParameterSpool<A, V>) parmSpool.clone();
        }
        return retValue;
    }
}
