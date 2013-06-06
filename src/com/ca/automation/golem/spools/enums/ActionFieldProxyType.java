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
    
    protected static Map<Class<?>,ActionFieldProxyType> annotationMap;
    
    static{
        annotationMap = new HashMap<Class<?>, ActionFieldProxyType>();
        annotationMap.put(RunContext.class, Contexts);
        annotationMap.put(RunParameter.class, Parameters);
        annotationMap.put(RunConnection.class, Connections);
    }

    public static Map<Class<?>, ActionFieldProxyType> getAnnotationMap() {
        return annotationMap;
    }
    
    
}
