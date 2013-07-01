/*
 */
package com.ca.automation.testClasses.actions.wrong;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class WrongActionNoDefCOnstructor {

    public Integer test;

    public WrongActionNoDefCOnstructor(Integer test) {
        this.test = test;
    }
    

    @Run
    public void dummyRun(){
        
    }
}
