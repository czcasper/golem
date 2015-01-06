/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
