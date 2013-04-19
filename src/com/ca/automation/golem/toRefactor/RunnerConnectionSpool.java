/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.toRefactor;

import com.ca.automation.golem.context.actionInterfaces.RunnerConectionSpool;
import com.ca.automation.golem.context.actionInterfaces.RunnerConnection;
import java.util.LinkedHashMap;

/**
 *
 * @author maslu02
 */
public class RunnerConnectionSpool extends LinkedHashMap<Object, RunnerConnection> implements RunnerConectionSpool {

    /**
     *
     */
    public RunnerConnectionSpool() {
        super(16, 0.75f, true);
    }
}
