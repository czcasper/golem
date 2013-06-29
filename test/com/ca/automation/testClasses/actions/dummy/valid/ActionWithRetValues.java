/*
 */
package com.ca.automation.testClasses.actions.dummy.valid;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithRetValues {

    @RunParameter(retValue=true)
    private Number random;
    
    @Run
    public void generate(){
        random= Math.random();
    }
}
