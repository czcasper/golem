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
