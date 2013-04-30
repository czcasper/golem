/*
 */
package com.ca.automation.golem.spools.actions.testClass;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunContext;
import com.ca.automation.golem.annotations.methods.Run;

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
