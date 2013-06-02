/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.connections;

/**
 *
 * @author maslu02
 */
public interface Connection {

    public boolean open();
    public boolean close();
    public boolean isOpen();
}
