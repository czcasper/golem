/*
 */
package cz.a_d.automation.golem.spools.actions;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import java.util.List;

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
        if(actions instanceof AddressArrayList){
            this.actions = actions;
        }else {
            this.actions = new AddressArrayList<>(actions);
        }
//        this.actions = new AddressArrayList<>(actions.size());
//        ActionInformationSpool<Object> spool = ActionInformationSpoolImpl.getGlobal();
//        for (A action : actions) {
//            if (spool.isValidAction(action)) {
//                this.actions.add(action);
//            } else {
//                Logger.getLogger(SimpleActionStream.class.getName()).log(Level.INFO, "Action {0} is not valid runner action.", action.toString());
//            }
//        }
//        if (this.actions.isEmpty()) {
//            throw new NullPointerException("List doesnt containsFrom valid actions");
//        }
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
