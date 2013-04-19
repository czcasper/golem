/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.actionInterfaces;

/**
 *
 * @author basad01
 */
public interface RunCond {

    /**
     * Is Central Stop set in place?
     *
     * @return true if central stop is required
     */
    public boolean getCentralStop();

    /**
     * Sets value of the central stop
     * @param stop 
     */
    public void setCentralStop(boolean stop);

    /**
     * How many steps to skip
     *
     * @return number of steps to skip
     */
    public int getSkipBy();

    /**
     * Sets how many steps to skip
     *
     * @param stepCount
     */
    public void setSkipBy(int stepCount);

}
