/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunParameter;
import cz.a_d.automation.golem.annotations.methods.Init;
import cz.a_d.automation.golem.annotations.methods.Run;
import cz.a_d.automation.golem.annotations.methods.Validate;

/**
 *
 * @author casper
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
    
    @Validate
    public boolean valid(){
        return (text!=null);
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
