/*
 */
package com.ca.automation.testClasses.actions.wrong;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Validate;

/**
 *
 * @author maslu02
 */
@RunAction
public class WrongNoRunMethod {

    @RunParameter
    protected int i;

    @Init
    public int getI() {
        return i;
    }

    @Validate
    public void setDefI() {
        this.i = 10;
    }
        
}
