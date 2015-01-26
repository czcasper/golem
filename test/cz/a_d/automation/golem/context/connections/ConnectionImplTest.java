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
package cz.a_d.automation.golem.context.connections;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author casper
 */
public class ConnectionImplTest {

    protected static File testFile;

    public ConnectionImplTest() throws IOException {
        testFile = File.createTempFile("connectionTest", "");
    }

    @AfterClass
    public static void delete() {
        testFile.delete();
    }

    /**
     * Test of reset method, of class ConnectionImpl.
     * @throws java.lang.Exception
     */
    @Test
    public void testReset_GetCurrent() throws Exception {
        /**
         * Initialize and test protection for null parameter value.
         */
        URL url = testFile.toURI().toURL();
        URLConnection connection = null;
        Connection instance = GolemConnectionFactory.getGlobalFactory().open(url);
        URLConnection current = instance.getCurrent();
        instance.reset(connection);
        assertSame(current, instance.getCurrent());

        /**
         * Test reset with valid connection.
         */
        connection = url.openConnection();
        assertNotSame(current, connection);
        instance.reset(connection);
        assertSame(connection, instance.getCurrent());
        instance.close();
    }

    /**
     * Test of isOpen method, of class ConnectionImpl.
     */
    @Test
    public void testIsOpen() throws MalformedURLException, IOException {
        /**
         * Initialize and test testing state of connection.
         */
        URL url = testFile.toURI().toURL();
        Connection instance = GolemConnectionFactory.getGlobalFactory().open(url);
        assertTrue(instance.isOpen());

        instance.close();
        assertFalse(instance.isOpen());
    }

    /**
     * Test of open method, of class ConnectionImpl.
     * @throws java.lang.Exception
     */
    @Test
    public void testOpen_Close() throws Exception {
        /**
         * Initialize and test openning and closing connection with same
         * configuration.
         */
        URL url = testFile.toURI().toURL();
        Connection instance = GolemConnectionFactory.getGlobalFactory().open(url);
        assertTrue(instance.isOpen());
        instance.open();
        assertTrue(instance.isOpen());
        instance.close();
        assertFalse(instance.isOpen());
    }

    /**
     * Test of getChannel method, of class ConnectionImpl.
     * @throws java.io.IOException
     */
    @Test
    public void testGetChannel() throws IOException {
        /**
         * Initialize and test interaction of method with reset.
         */
        URL url = testFile.toURI().toURL();
        Connection instance = GolemConnectionFactory.getGlobalFactory().open(url);
        ConnectionChannel result = instance.getChannel();
        assertNotNull(result);
        
        /**
         * Test if instance is same in other calls.
         */
        Random r = new Random();
        int count = r.nextInt(50)+20;
        for(int i=0;i<count;i++){
            ConnectionChannel tmpResult = instance.getChannel();
            assertSame(result, tmpResult);
        }
        /**
         * Test channel instance after reset.
         */
        URLConnection tmpConnection = url.openConnection();
        instance.reset(tmpConnection);
        assertSame(result, instance.getChannel());
    }
}
