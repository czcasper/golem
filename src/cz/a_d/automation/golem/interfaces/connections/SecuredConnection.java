/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.connections;

/**
 *
 * @author maslu02
 */
public interface SecuredConnection {

    public boolean login(String userName, String password);

    public boolean logout();
}
