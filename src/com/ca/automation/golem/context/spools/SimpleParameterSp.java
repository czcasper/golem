/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.spools;

import com.ca.automation.golem.context.actionInterfaces.SimpleParameterSpool;
import java.util.LinkedHashMap;

/**
 *
 * @author maslu02
 */
public class SimpleParameterSp extends LinkedHashMap<String, Object> implements SimpleParameterSpool {

    /**
     *
     */
    public SimpleParameterSp() {
        super(16, 0.75f, true);
    }
    
    
}
