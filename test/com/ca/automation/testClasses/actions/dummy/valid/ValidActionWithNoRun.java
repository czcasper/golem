/*
 */
package com.ca.automation.testClasses.actions.dummy.valid;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Validate;

/**
 *
 * @author maslu02
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
