/*
 */
package com.ca.automation.testClasses.actions.dummy.valid;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunContext;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
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
