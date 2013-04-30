/*
 */
package com.ca.automation.golem.spools.actions.testClass;

import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;

/**
 *
 * @author maslu02
 */
@RunAction
public class ActionWithMembers {

    @RunParameter
    protected String text;
    @RunParameter
    private boolean upperCase=false;
    
    @Init
    public void initText(){
        if(text.isEmpty()){
            text = "Hello World !!";
        }
    }
    
    @Run
    public void printText(){
        String print=text;
        if(upperCase){
            print = print.toUpperCase();
        }
        System.out.print(print);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }
    
}
