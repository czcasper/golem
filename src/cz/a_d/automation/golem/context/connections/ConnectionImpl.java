/*
 */
package cz.a_d.automation.golem.context.connections;

import cz.a_d.automation.golem.context.connections.channels.ConnectionChannelImpl;
import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.IOException;
import java.net.URLConnection;

/**
 * Internal Golem implementation of operations with connections to resource from actions provided by Golem connection factory.
 * Implementation is wrapping URL connect method and using URL connection provided by GolemURLStreamHandlerFactory.
 *
 * @author casper
 */
public class ConnectionImpl implements Connection {

    /**
     * Instance of wrapper for streams provided by URL connection. This allows to use Channels from java.nio for dealing with resources.
     */
    protected ConnectionChannel connectionChannel;

    /**
     * Creating new instance of Golem connection implementation.
     *
     * @param connection instance of URLConnection which will be wrapped by this constructed instance.
     *
     * @throws IOException          if an I/O error occurs while creating the connection.
     * @throws NullPointerException if connetion used for construction is null.
     */
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
