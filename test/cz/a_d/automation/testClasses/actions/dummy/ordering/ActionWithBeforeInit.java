/*
 */
package cz.a_d.automation.testClasses.actions.dummy.ordering;

import cz.a_d.automation.golem.annotations.methods.Init;

/**
 *
 * @author maslu02
 */
// TODO Documentation: Create JavaDoc on class level and point to test where class is used.
public class ActionWithBeforeInit {

    @Init(order=-1)
    public void beforeInit(){
        
    }
}
