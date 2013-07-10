/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.connections;

import java.nio.channels.ByteChannel;

/**
 *
 * @author maslu02
 */
public interface Connection {

    public boolean reOpen();

    public boolean close();

    public boolean isOpen();

    public ByteChannel getChannel();
}
