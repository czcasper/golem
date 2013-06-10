/*
 */
package com.ca.automation.golem.spools.actions.testClass;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithAfterRun {

    @Run(order=1)
    public void afterRun(){
    }
}
