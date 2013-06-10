/*
 */
package com.ca.automation.golem.spools.actions.testClass;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.methods.Init;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithAfterInit {

    @Init(order=1)
    public void afterInit(){
        
    }
}
