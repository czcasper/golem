/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunContext;
import cz.a_d.automation.golem.annotations.methods.Run;

/**
 *
 * @author casper
 */
@RunAction
public class ActionWithPointerToContext {

    @RunContext(pointer="1")
    protected String dummy;
    
    @Run
    public void run(){
        
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }    
}
