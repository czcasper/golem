/*
 */
package cz.a_d.automation.golem.interfaces.connections;

import cz.a_d.automation.golem.interfaces.connections.channels.CommandChannel;

/**
 * Interface define extension to standard connection for sending commands to resource which is able to accept commands.
 *
 * @author casper
 */
public interface TerminalConnection extends Connection {

    /**
     * Getter to access implementation of channel with support command execution.
     *
     * @return instance of command channel connected with connection.
     */
    public CommandChannel getCommandChannel();
}
