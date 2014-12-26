/*
 */
package cz.a_d.automation.golem.context.connections.channels;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 *
 * @author casper
 */
public class ConnectionChannelImpl implements ConnectionChannel {

    protected URLConnection connection;
    protected ReadableByteChannel input = null;
    protected WritableByteChannel output = null;

    public ConnectionChannelImpl(URLConnection connection) throws IOException {
        if (connection == null) {
            throw new NullPointerException("Connection channe cannot be initializedby null connection");
        }
        if (!((connection.getDoInput()) || (connection.getDoOutput()))) {
            throw new IOException("Invalid URL connection:" + connection.toString() + " doesn't provides any type of comunication stream.");
        }
        this.connection = connection;
        initChannelStreams();
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        int retValue = 0;
        if ((dst != null) && (input != null)) {
            retValue = input.read(dst);
        } else if (input == null) {
            retValue = -1;
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
        /**
         * Order of closing is mandatory, in case when channels are pointing to same resource output needs to be for allow auto flush
         * feature closed in first line.
         */
        if (output != null && output.isOpen()) {
            output.close();
        }
        if (input != null && input.isOpen()) {
            input.close();
        }
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        int retValue = 0;
        if ((src != null) && (output != null)) {
            retValue = output.write(src);
            connection.getOutputStream().flush();
        } else if (output == null) {
            retValue = -1;
        }
        return retValue;
    }

    @Override
    public boolean isReadable() {
        return connection.getDoInput();
    }

    @Override
    public boolean isWriteable() {
        return connection.getDoOutput();
    }

    @Override
    public void refresh(URLConnection connection) throws IOException {
        if (connection != null) {
            this.connection = connection;
            input = null;
            output = null;
            initChannelStreams();
        }
    }

    @Override
    public URLConnection getCurrentConnection() {
        return connection;
    }

    protected final void initChannelStreams() throws IOException {
        if (input == null && connection.getDoInput()) {
            InputStream inputStream = connection.getInputStream();
            if (inputStream != null) {
                input = Channels.newChannel(inputStream);
            }
        }
        if (output == null && connection.getDoOutput()) {
            OutputStream outputStream = connection.getOutputStream();
            if (outputStream != null) {
                output = Channels.newChannel(outputStream);
            }
        }
    }
}
