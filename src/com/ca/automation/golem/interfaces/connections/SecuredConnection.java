/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.connections;

/**
 *
 * @author maslu02
 */
public interface SecuredConnection extends Connection {
    
    public boolean login(String userName,String password);
    public boolean logout();
    
}
