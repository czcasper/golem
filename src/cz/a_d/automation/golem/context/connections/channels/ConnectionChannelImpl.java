/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.context.connections.channels;

import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of interface for interaction with resources defined by URL. Streams provided by URL are converted into implementation of
 * byte channel.
 *
 * @author casper
 */
public class ConnectionChannelImpl implements ConnectionChannel {

    /**
     * Instance of URL connection which is used to provide access to resource data.
     */
    protected URLConnection connection;

    /**
     * Instance of channel used for read operations. Can be null in case when channel is not initialized for reading or connection doesn't
     * support read operations.
     */
    protected ReadableByteChannel input = null;

    /**
     * Instance of channel used for write operations. Can be null in case when connection doesn't support write operations.
     */
    protected WritableByteChannel output = null;

    /**
     * Construct new instance of connection channel implementation from URLconnection. Connection needs to be open to successfully
     * initialize channels for I/O operations.
     *
     * @param connection connection which will be wrapped by this channel and used to perform I/O resource. Cannot be null.
     *
     * @throws IOException          if some other I/O error occurs.
     * @throws NullPointerException if connection is null.
     */
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

    /**
     * Initializing channels providing I/O operations from URL connection.
     *
     * @throws IOException if some other I/O error occurs.
     */
    protected final void initChannelStreams() throws IOException {
        if (input == null && connection.getDoInput()) {
            try {
                InputStream inputStream = connection.getInputStream();
                if (inputStream != null) {
                    input = Channels.newChannel(inputStream);
                }
            } catch (UnknownServiceException ex) {
                Logger.getLogger(ConnectionChannelImpl.class.getName()).log(Level.INFO, "Connection doesn't support input", ex.getLocalizedMessage());
            }
        }
        if (output == null && connection.getDoOutput()) {
            try {
                OutputStream outputStream = connection.getOutputStream();
                if (outputStream != null) {
                    output = Channels.newChannel(outputStream);
                }
            } catch (UnknownServiceException ex) {
                Logger.getLogger(ConnectionChannelImpl.class.getName()).log(Level.INFO, "Connection doesn't support output", ex.getLocalizedMessage());
            }
        }
    }
}
