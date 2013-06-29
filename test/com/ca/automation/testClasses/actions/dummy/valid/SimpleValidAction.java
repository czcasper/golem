/*
 */
package com.ca.automation.testClasses.actions.dummy.valid;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class SimpleValidAction {

    @Run
    public void run(){
        System.out.println("Hello World!");
    }
}
