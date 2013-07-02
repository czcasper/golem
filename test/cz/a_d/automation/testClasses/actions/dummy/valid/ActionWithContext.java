/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunContext;
import cz.a_d.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithContext extends ValidActionWithNoRun {
    
    @RunContext
    private Integer dummyContext;
    
    @Run(order= Integer.MAX_VALUE)
    protected boolean applyContext(){
        return true;
    }

}
