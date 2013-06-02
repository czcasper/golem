/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.connections;

import java.nio.channels.ByteChannel;

/**
 *
 * @author maslu02
 */
public interface StreamConnection extends ByteChannel {

    public boolean writeChannel(ByteChannel ch,String remoteDst);
    public boolean readChannel(ByteChannel ch, String remoteDst);
    
}
