/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.methods.Run;

/**
 *
 * @author casper
 */
@RunAction
public class SimpleValidAction {

    @Run
    public void run(){
        System.out.println("Hello World!");
    }
}
