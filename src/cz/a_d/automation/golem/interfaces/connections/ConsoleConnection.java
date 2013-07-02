/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.connections;

/**
 *
 * @author maslu02
 */
public interface ConsoleConnection {

    public int submitCommand(CharSequence command);
    public CharSequence takeAnswer();
    
}
