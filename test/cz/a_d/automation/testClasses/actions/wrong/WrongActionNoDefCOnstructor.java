/*
 */
package cz.a_d.automation.testClasses.actions.wrong;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.methods.Run;

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
