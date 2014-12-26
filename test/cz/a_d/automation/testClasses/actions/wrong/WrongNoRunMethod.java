/*
 */
package cz.a_d.automation.testClasses.actions.wrong;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunParameter;
import cz.a_d.automation.golem.annotations.methods.Init;
import cz.a_d.automation.golem.annotations.methods.Validate;

/**
 *
 * @author casper
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
