/*
 */
package cz.a_d.automation.golem.interfaces.connections.channels;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.ByteChannel;

/**
 * Interface describing Golem interaction with resources defined by URL. Streams provided by URL are converted into implementation of byte
 * channel.
 *
 * @author casper
 */
public interface ConnectionChannel extends ByteChannel {

    /**
     * Test if channel is able to perform read operations.
     *
     * @return true in case when channel is able to provide read operation, otherwise return false.
     */
    public boolean isReadable();

    /**
     * Test is channel is able to perform write operations.
     *
     * @return true in case when channel is able to provide write operation, otherwise return false.
     */
    public boolean isWriteable();

    /**
     * Changing currently used URL connection to connection from argument.
     *
     * @param connection connection which will be used to change currently used connection.Must be different from null, otherwise currently
     *                   used connection will be not changed.
     * @throws IOException if some other I/O error occurs.
     */
    public void refresh(URLConnection connection) throws IOException;

    /**
     * Getter to access instance of currently used connection.
     *
     * @return connection which is used to accessing resource. Never return null.
     */
    public URLConnection getCurrentConnection();
}
