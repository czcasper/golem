/*
 */
package cz.a_d.automation.golem.context.connections;

import cz.a_d.automation.golem.context.connections.channels.ConnectionChannelImpl;
import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.IOException;
import java.net.URLConnection;

/**
 *
 * @author casper
 */
public class ConnectionImpl implements Connection {

    protected ConnectionChannel connectionChannel;

    public ConnectionImpl(URLConnection connection) throws IOException {
        if (connection == null) {
            throw new NullPointerException("Connection cannot be initialized by by null URL connection");
        }
        connectionChannel = new ConnectionChannelImpl(connection);
    }

    @Override
    public void reset(URLConnection connection) throws IOException {
        if (((connection != null) && ((connectionChannel.getCurrentConnection() != connection)) || (!isOpen()))) {
            if (isOpen()) {
                close();
            }
            connectionChannel.refresh(connection);
        }
    }

    @Override
    public boolean isOpen() {
        return connectionChannel.isOpen();
    }

    @Override
    public void open() throws IOException {
        URLConnection currentConnection = connectionChannel.getCurrentConnection();
        currentConnection.connect();
        connectionChannel.refresh(currentConnection);
    }

    @Override
    public void close() throws IOException {
        connectionChannel.close();
    }

    @Override
    public ConnectionChannel getChannel() {
        return connectionChannel;
    }

    @Override
    public URLConnection getCurrent() {
        return connectionChannel.getCurrentConnection();
    }
}
