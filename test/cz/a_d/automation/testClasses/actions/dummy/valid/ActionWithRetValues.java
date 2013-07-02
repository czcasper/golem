/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunParameter;
import cz.a_d.automation.golem.annotations.methods.Run;

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
