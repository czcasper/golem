/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunConnection;
import cz.a_d.automation.golem.annotations.methods.Run;

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
