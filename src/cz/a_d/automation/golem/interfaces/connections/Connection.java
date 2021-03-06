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
package cz.a_d.automation.golem.interfaces.connections;

import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.Closeable;
import java.io.IOException;
import java.net.URLConnection;

/**
 * Interface defining operations with connections to resource from actions provided by Golem connection factory. This is unified approach
 * which is covering all resources used by Golem actions.
 *
 * @author casper
 */
public interface Connection extends Closeable {

    /**
     * Testing if connection is open.
     *
     * @return true in case when connection is open and ready to be used for I/O operations, otherwise false.
     */
    public boolean isOpen();

    /**
     * Connect URL defined by constructor with physical resource.
     *
     * @throws IOException if an I/O error occurs while opening the connection.
     */
    public void open() throws IOException;

    @Override
    public void close() throws IOException;

    /**
     * Changing internally used URL connection to other instance. This leads to reusability of same object and allows change used resource
     * without changing instance used by Golem actions.
     *
     * @param connection instance of URLConnection which will be replacing currently used instance. In case when it is null original
     *                   connection is not changed.
     * @throws IOException if an I/O error occurs while opening the connection.
     */
    public void reset(URLConnection connection) throws IOException;

    /**
     * Getter to access currently used URL connection instance.
     *
     * @return currently used instance of URL connection. Never return null value.
     */
    public URLConnection getCurrent();

    /**
     * Getter to access resources provided by connection in format of Channel.
     *
     * @return instance of channel connected with streams provided by URL connection.
     */
    public ConnectionChannel getChannel();
}
