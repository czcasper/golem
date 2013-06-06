/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.enums;

import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public enum ActionMethodProxyType {

    Initialize, Run, Validate;
    
    protected static Map<Class<?>,ActionMethodProxyType> annotationMap;
    protected static ActionMethodProxyType[] callOrder={Initialize,Run,Validate};
    
    static{
        annotationMap = new HashMap<Class<?>, ActionMethodProxyType>();
        annotationMap.put(Init.class, Initialize);
        annotationMap.put(Run.class, Run);
        annotationMap.put(Validate.class, Validate);
    }

    public static Map<Class<?>, ActionMethodProxyType> getAnnotationMap() {
        return annotationMap;
    }
    
    public static ActionMethodProxyType[] getCallOrder(){
        return callOrder;
    }
}
