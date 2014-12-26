/*
 */
package cz.a_d.automation.testClasses.actions.dummy.ordering;

import cz.a_d.automation.golem.annotations.methods.Validate;

/**
 *
 * @author casper
 */
// TODO Documentation: Create JavaDoc on class level and point to test where class is used.
public class ActionWithAfterValidate {

    @Validate(order=1)
    public void afterValid(){
        
    }
}
