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
 * @author maslu02
 */
public class ConnectionImpl implements Connection {

    protected URLConnection connection;
    protected ConnectionChannel connectionChannel;

    public ConnectionImpl(URLConnection connection) throws IOException {
        if (connection == null) {
            throw new NullPointerException("Connection cannot be initialized by by null URL connection");
        }
        this.connection = connection;
        connectionChannel = new ConnectionChannelImpl(this, connection);
    }

    @Override
    public void reset(URLConnection connection) throws IOException {
        if (((connection != null) && ((this.connection != connection)) || (!isOpen()))) {
            if (isOpen()) {
                close();
            }
            this.connection = connection;
            connectionChannel.refresh(connection);
        }
    }

    @Override
    public boolean isOpen() {
        return connectionChannel.isOpen();
    }

    @Override
    public void open() throws IOException {
        connection.connect();
        connectionChannel.refresh(connection);
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
        return connection;
    }
}
