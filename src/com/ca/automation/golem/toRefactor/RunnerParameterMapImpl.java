/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.toRefactor;

import com.ca.automation.golem.context.actionInterfaces.RunParameterMap;
import java.util.LinkedHashMap;

/**
 *
 * @author maslu02
 */
public class RunnerParameterMapImpl extends LinkedHashMap<String, Object>  implements RunParameterMap {

    /**
     *
     */
    public RunnerParameterMapImpl() {
        super(16, 0.75f, true);
    }
    
    
}
