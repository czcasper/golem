/*
 */
package com.ca.automation.golem.spools.actions.testClass;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.methods.Validate;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithAfterValidate {

    @Validate(order=1)
    public void afterValid(){
        
    }
}
