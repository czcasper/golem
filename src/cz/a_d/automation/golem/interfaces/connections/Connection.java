/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.connections;

import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.IOException;
import java.net.URLConnection;

/**
 *
 * @author casper
 */
public interface Connection {

    public boolean isOpen();

    public void open() throws IOException;

    public void close() throws IOException;

    public void reset(URLConnection connection) throws IOException;
    
    public URLConnection getCurrent();

    public ConnectionChannel getChannel();
}
