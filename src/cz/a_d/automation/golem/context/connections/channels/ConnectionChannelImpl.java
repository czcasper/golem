/*
 */
package cz.a_d.automation.golem.context.connections.channels;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.IOException;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
public class ConnectionChannelImpl implements ConnectionChannel {

    protected Connection parent;
    protected ReadableByteChannel input;
    protected WritableByteChannel output;
    protected boolean recursive = true;

    public ConnectionChannelImpl(Connection parent, URLConnection connection) throws IOException {
        if ((parent == null) || (connection == null)) {
            throw new NullPointerException("Connection channe cannot be initializedby null connection");
        }
        this.parent = parent;
        initChannelStreams(connection);
        if ((input == null) && (output == null)) {
            throw new IOException("Invalid URL connection:" + connection.toString() + " doesn't provides any type of comunication stream.");
        }
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        int retValue = -1;
        if ((dst != null) && (input != null)) {
            retValue = input.read(dst);
        }
        return retValue;
    }

    @Override
    public boolean isOpen() {
        boolean retValue = false;
        if ((input != null) && (input.isOpen())) {
            retValue = true;
        } else if ((output != null) && (output.isOpen())) {
            retValue = true;
        }
        return retValue;
    }

    @Override
    public void close() throws IOException {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        int retValue = -1;
        if ((src != null) && (output != null)) {
            retValue = output.write(src);
        }
        return retValue;
    }

    @Override
    public boolean isReadable() {
        return (input != null);
    }

    @Override
    public boolean isWriteable() {
        return (output != null);
    }

    @Override
    public void refresh(URLConnection connection) throws IOException {
        if (recursive) {
            recursive = false;
            parent.reset(connection);
            return;
        }
        initChannelStreams(connection);
        recursive = true;
    }

    protected final void initChannelStreams(URLConnection connection) throws IOException {
        try {
            input = Channels.newChannel(connection.getInputStream());
        } catch (UnknownServiceException ex) {
            input = null;
            Logger.getLogger(ConnectionChannelImpl.class.getName()).log(Level.FINE, "Connection:{0} doesn''t implement input stream. Message from connection:{1}", new Object[]{connection.toString(), ex.getMessage()});
        }
        try {
            output = Channels.newChannel(connection.getOutputStream());
        } catch (UnknownServiceException ex) {
            output = null;
            Logger.getLogger(ConnectionChannelImpl.class.getName()).log(Level.FINE, "Connection:{0} doesn''t implement output stream. Message from connection:{1}", new Object[]{connection.toString(), ex.getMessage()});
        }

    }
}
