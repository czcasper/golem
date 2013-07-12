/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.connections.channels;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.ByteChannel;

/**
 *
 * @author maslu02
 */
public interface ConnectionChannel extends ByteChannel {

    public boolean isReadable();

    public boolean isWriteable();

    public void refresh(URLConnection connection) throws IOException;
}
