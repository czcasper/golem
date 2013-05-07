/*
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.interfaces.ActionStream;
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
public class SimpleActionStream implements ActionStream<Object, String, Object> {

    protected Map<String, Object> parmSpool;
    protected List<Object> actions;
    protected ResetableIterator<Object> it;

    public SimpleActionStream(List<Object> actions) {
        if (actions == null) {
            throw new NullPointerException("Action stream cannot be initializet by null array of actions");
        }
        this.actions = new AddressArrayList<Object>(actions.size());
        ActionInformationSpool spool = ActionInformationSpool.getDefaultInstance();
        for (Object action : actions) {
            if (spool.isAction(action)) {
                this.actions.add(action);
            } else {
                Logger.getLogger(SimpleActionStream.class.getName()).log(Level.INFO, "Action {0} is not valid runner action.", action.toString());
            }
        }
        if (this.actions.isEmpty()) {
            throw new NullPointerException("List doesnt contains valid actions");
        }
        it = new ResetableIterator<Object>(this.actions.iterator());
    }

    @Override
    public List<Object> getActionList() {
        return actions;
    }

    @Override
    public void setParameter(Map<String, Object> actionParams) {
        this.parmSpool = actionParams;
    }

    @Override
    public Map<String, Object> getParameterMap() {
        return parmSpool;
    }

    @Override
    public ResetableIterator<Object> resetableIterator() {
        return it;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SimpleActionStream retValue = (SimpleActionStream) super.clone();
        retValue.it = new ResetableIterator<Object>(retValue.actions.iterator());
        if (parmSpool != null) {
            retValue.parmSpool = new LinkedHashMap<String, Object>(parmSpool);
        }
        return retValue;
    }
}
