/*
 */
package cz.a_d.automation.golem.context.connections.channels;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

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
        input = Channels.newChannel(connection.getInputStream());
        output = Channels.newChannel(connection.getOutputStream());
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
            parent.reset(connection);
            recursive = false;
            return;
        }
        input = Channels.newChannel(connection.getInputStream());
        output = Channels.newChannel(connection.getOutputStream());
        recursive = true;
    }
}
