/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.context.connections;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import java.io.File;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author casper
 */
public class GolemConnectionFactoryTest {

    public GolemConnectionFactoryTest() {
    }

    /**
     * Test of getGlobalFactory method, of class GolemConnectionFactory.
     */
    @Test
    public void testGetGlobalFactory() {
        /**
         * Initialize and test use case common for global factories.
         */
        GolemConnectionFactory instanceOne = GolemConnectionFactory.getGlobalFactory();
        assertNotNull(instanceOne);
        GolemConnectionFactory instanceTwo = GolemConnectionFactory.getGlobalFactory();
        assertNotNull(instanceTwo);
        assertSame(instanceOne, instanceTwo);
    }

    /**
     * Test of open method, of class GolemConnectionFactory.
     */
    @Test
    public void testOpen_URI_Proxy() throws Exception {
        /**
         * Initialize and test protection for null parameters value.
         */
        URI uri = null;
        Proxy proxy = null;
        Connection result = GolemConnectionFactory.open(uri, proxy);
        assertNull(result);

        proxy = Proxy.NO_PROXY;
        result = GolemConnectionFactory.open(uri, proxy);
        assertNull(result);

        /**
         * Intialize and open connection into file.
         */
        File testFile = File.createTempFile("golemConnectionFactory", "open_uri");
        try {
            uri = testFile.toURI();
            result = GolemConnectionFactory.open(uri, proxy);
            assertNotNull(result);
            assertTrue(result.isOpen());
            assertNotNull(result.getChannel());
            assertTrue(result.getChannel().isReadable());
            assertFalse(result.getChannel().isWriteable());

            /**
             * Test if method open will opens new connection into same file.
             */
            Connection secondResult = GolemConnectionFactory.open(uri, proxy);
            assertNotNull(secondResult);
            assertNotSame(result, secondResult);
            assertTrue(secondResult.isOpen());
            assertNotNull(secondResult.getChannel());
            assertTrue(secondResult.getChannel().isReadable());
            assertFalse(secondResult.getChannel().isWriteable());

            /**
             * Close connection openned by test.
             */
            result.close();
            secondResult.close();
        } finally {
            testFile.delete();
        }
    }

    /**
     * Test of open method, of class GolemConnectionFactory.
     */
    @Test
    public void testOpen_URL_Proxy() throws Exception {
        /**
         * Initialize and test protection for null parameters value.
         */
        URL url = null;
        Proxy proxy = null;
        Connection result = GolemConnectionFactory.open(url, proxy);
        assertNull(result);

        proxy = Proxy.NO_PROXY;
        result = GolemConnectionFactory.open(url, proxy);
        assertNull(result);

        /**
         * Intialize and open connection into file.
         */
        File testFile = File.createTempFile("golemConnectionFactory", "open_uri");
        try {
            url = testFile.toURI().toURL();
            result = GolemConnectionFactory.open(url, proxy);
            assertNotNull(result);
            assertTrue(result.isOpen());
            assertNotNull(result.getChannel());
            assertTrue(result.getChannel().isReadable());
            assertFalse(result.getChannel().isWriteable());

            /**
             * Test if method open will opens new connection into same file.
             */
            Connection secondResult = GolemConnectionFactory.open(url, proxy);
            assertNotNull(secondResult);
            assertNotSame(result, secondResult);
            assertTrue(secondResult.isOpen());
            assertNotNull(secondResult.getChannel());
            assertTrue(secondResult.getChannel().isReadable());
            assertFalse(secondResult.getChannel().isWriteable());

            /**
             * Close connection openned by test.
             */
            result.close();
            secondResult.close();
        } finally {
            testFile.delete();
        }
    }

    /**
     * Test of open method, of class GolemConnectionFactory.
     */
    @Test
    public void testOpen_URI() throws Exception {
        /**
         * Initialize and test protection for null paramter value.
         */
        URI uri = null;
        Connection result = GolemConnectionFactory.open(uri);
        assertNull(result);

        /**
         * Initialize and test open valid connetion to file.
         */
        File testFile = File.createTempFile("golemConnectionFactory", "open_uri");
        try {
            uri = testFile.toURI();
            result = GolemConnectionFactory.open(uri);
            assertNotNull(result);
            assertTrue(result.isOpen());
            assertNotNull(result.getChannel());
            assertTrue(result.getChannel().isReadable());
            assertFalse(result.getChannel().isWriteable());

            /**
             * Test if method open will opens new connection into same file.
             */
            Connection secondResult = GolemConnectionFactory.open(uri);
            assertNotNull(secondResult);
            assertNotSame(result, secondResult);
            assertTrue(secondResult.isOpen());
            assertNotNull(secondResult.getChannel());
            assertTrue(secondResult.getChannel().isReadable());
            assertFalse(secondResult.getChannel().isWriteable());

            /**
             * Close connection openned by test.
             */
            result.close();
            secondResult.close();
        } finally {
            /**
             * Delete file used for testing.
             */
            testFile.delete();
        }

    }

    /**
     * Test of open method, of class GolemConnectionFactory.
     */
    @Test
    public void testOpen_URL() throws Exception {
        /**
         * Initialize and test protection for null paramter value.
         */
        URL url = null;
        Connection result = GolemConnectionFactory.open(url);
        assertNull(result);

        /**
         * Initialize and test open valid connetion to file.
         */
        File testFile = File.createTempFile("golemConnectionFactory", "open_uri");
        try {
            url = testFile.toURI().toURL();
            result = GolemConnectionFactory.open(url);
            assertNotNull(result);
            assertTrue(result.isOpen());
            assertNotNull(result.getChannel());
            assertTrue(result.getChannel().isReadable());
            assertFalse(result.getChannel().isWriteable());

            /**
             * Test if method open will opens new connection into same file
             */
            Connection secondResult = GolemConnectionFactory.open(url);
            assertNotNull(secondResult);
            assertNotSame(result, secondResult);
            assertTrue(secondResult.isOpen());
            assertNotNull(secondResult.getChannel());
            assertTrue(secondResult.getChannel().isReadable());
            assertFalse(secondResult.getChannel().isWriteable());

            /**
             * Close connection openned by test.
             */
            result.close();
            secondResult.close();
        } finally {
            /**
             * Delete file used for testing.
             */
            testFile.delete();
        }
    }
}
