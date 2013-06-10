/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.enums;

import com.ca.automation.golem.annotations.fields.RunConnection;
import com.ca.automation.golem.annotations.fields.RunContext;
import com.ca.automation.golem.annotations.fields.RunParameter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public enum ActionFieldProxyType {

    Parameters, Connections, ReturnValues, Contexts;
 
    public static ActionFieldProxyType getType(Class<? extends Annotation> annotation){
        ActionFieldProxyType retValue = null;
        if(annotation!=null){
            if(RunParameter.class.equals(annotation)){
                retValue = Parameters;
            }else if(RunConnection.class.equals(annotation)){
                retValue = Connections;
            }else if (RunContext.class.equals(annotation)){
                retValue = Contexts;
            }
        }
        return retValue;
    }
    
    public Class<? extends Annotation> getAnnotation() {
        Class<? extends Annotation> retValue = null;
        switch (this) {
            case Parameters:
                retValue = RunParameter.class;
                break;
            case Connections:
                retValue = RunConnection.class;
                break;
            case Contexts:
                retValue = RunContext.class;
                break;
            case ReturnValues:
                retValue = null;
                break;
        }
        return retValue;
    }
}
