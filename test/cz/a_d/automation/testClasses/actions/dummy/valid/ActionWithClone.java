/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.methods.Run;

/**
 *
 * @author casper
 */
@RunAction
public class ActionWithClone implements Cloneable {

    protected String clone;
    
    @Run
    public void dummyRun(){
        
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    
}
