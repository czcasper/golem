/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.connections;

import cz.a_d.automation.golem.interfaces.connections.channels.CommandChannel;

/**
 *
 * @author casper
 */
public interface TerminalConnection extends Connection {

    public CommandChannel getCommandChannel();
}
