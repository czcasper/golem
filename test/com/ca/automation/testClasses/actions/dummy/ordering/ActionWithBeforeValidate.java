/*
 */
package com.ca.automation.testClasses.actions.dummy.ordering;

import com.ca.automation.golem.annotations.methods.Validate;

/**
 *
 * @author maslu02
 */
// TODO Documentation: Create JavaDoc on class level and point to test where class is used.
public class ActionWithBeforeValidate {

    @Validate(order=-1)
    public void beforeValid(){
        
    }
}