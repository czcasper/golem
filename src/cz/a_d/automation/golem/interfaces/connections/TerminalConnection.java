/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
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
