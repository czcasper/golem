/*
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.ParameterKey;
import com.ca.automation.golem.spools.actions.ActionInformationSpool;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
public class SimpleActionStream<A,V> implements ActionStream<A, V> {

    protected AbstractSpool<A,ParameterKey,V> parmSpool;
    protected List<A> actions;
    protected ResetableIterator<A> it;

    public SimpleActionStream(List<A> actions) {
        if (actions == null) {
            throw new NullPointerException("Action stream cannot be initializet by null array of actions");
        }
        this.actions = new AddressArrayList<A>(actions.size());
        ActionInformationSpool spool = ActionInformationSpool.getDefaultInstance();
        for (A action : actions) {
            if (spool.isAction(action)) {
                this.actions.add(action);
            } else {
                Logger.getLogger(SimpleActionStream.class.getName()).log(Level.INFO, "Action {0} is not valid runner action.", action.toString());
            }
        }
        if (this.actions.isEmpty()) {
            throw new NullPointerException("List doesnt contains valid actions");
        }
        it = new ResetableIterator<A>(this.actions.iterator());
    }

    @Override
    public List<A> getActionList() {
        return actions;
    }

    @Override
    public void setParameter(AbstractSpool<A,ParameterKey,V> actionParams) {
        this.parmSpool = actionParams;
    }

    @Override
    public AbstractSpool<A,ParameterKey,V> getParameterMap() {
        return parmSpool;
    }

    @Override
    public ResetableIterator<A> resetableIterator() {
        return it;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SimpleActionStream<A,V> retValue = (SimpleActionStream<A,V>) super.clone();
        retValue.it = new ResetableIterator<A>(retValue.actions.iterator());
        if (parmSpool != null) {
            retValue.parmSpool = (AbstractSpool<A,ParameterKey,V>) parmSpool.clone();
        }
        return retValue;
    }
}
