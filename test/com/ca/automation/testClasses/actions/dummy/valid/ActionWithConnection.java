/*
 */
package com.ca.automation.testClasses.actions.dummy.valid;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunConnection;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithConnection {
    
    @RunConnection
    private Integer connectionSimulatat = null;
    
    @Run
    public void open(){
        
    }

}
