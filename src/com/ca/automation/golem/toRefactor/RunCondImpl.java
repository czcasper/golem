/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.toRefactor;

import com.ca.automation.golem.context.actionInterfaces.RunCond;

/**
 *
 * @author basad01
 */
public class RunCondImpl implements RunCond {

    private boolean centralStop = false;
    private int skipSteps = 0;
    private boolean isInitCritical = false;
    private boolean isRunCritical = false;
    private boolean isValidateCritical = true;

    @Override
    public boolean getCentralStop() {
        return centralStop;
    }

    @Override
    public void setCentralStop(boolean stop) {
        centralStop = stop;
    }

    @Override
    public int getSkipBy() {
        return skipSteps;
    }

    @Override
    public void setSkipBy(int stepCount) {
        skipSteps = stepCount;
    }
}
