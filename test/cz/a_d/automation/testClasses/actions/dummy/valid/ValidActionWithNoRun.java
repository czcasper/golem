/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunParameter;
import cz.a_d.automation.golem.annotations.methods.Init;
import cz.a_d.automation.golem.annotations.methods.Validate;

/**
 *
 * @author casper
 */
@RunAction
public class ValidActionWithNoRun extends ActionWithMembers {

    @RunParameter
    private String label;
    
    @Init
    public void initLabel(){
        if(label.isEmpty()){
            label = "Message:";
        }
    }
    
    @Validate
    public void reprint(){
        System.out.println(label+":"+text);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }    
}
