/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.toRefactor;

import com.ca.automation.golem.context.actionInterfaces.RunConectionSpool;
import com.ca.automation.golem.context.actionInterfaces.RunConnection;
import java.util.LinkedHashMap;

/**
 *
 * @author maslu02
 */
public class RunnerConnectionSpool extends LinkedHashMap<Object, RunConnection> implements RunConectionSpool {

    /**
     *
     */
    public RunnerConnectionSpool() {
        super(16, 0.75f, true);
    }
}
