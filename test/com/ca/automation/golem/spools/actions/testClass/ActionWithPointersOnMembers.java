/*
 */
package com.ca.automation.golem.spools.actions.testClass;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithPointersOnMembers {

    @RunParameter(pointer="1")
    protected String test;
    
    @Run
    public void run(){
        
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
        
}
