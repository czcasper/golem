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
package cz.a_d.automation.golem.protocol;

import cz.a_d.automation.golem.protocol.file.FileHelper;
import cz.a_d.automation.golem.protocol.file.FileURLConnection;
import cz.a_d.automation.golem.protocol.file.Handler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author casper
 */
public class GolemURLStreamHandlerFactoryTest {

    public GolemURLStreamHandlerFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of createURLStreamHandler method, of class GolemURLStreamHandlerFactory.
     */
    @Test
    public void testCreateURLStreamHandler() {
        GolemURLStreamHandlerFactory instance = new GolemURLStreamHandlerFactory();
        String protocol = "";
        URLStreamHandler result = instance.createURLStreamHandler(protocol);
        assertNull(result);
        protocol = "file";
        result = instance.createURLStreamHandler(protocol);
        assertNotNull(result);
        assertTrue(result instanceof Handler);

        protocol = "jar";
        result = instance.createURLStreamHandler(protocol);
        assertNotNull(result);
    }

    @Test
    public void testIntegrationWithSystem() throws IOException{
        File tmpFile = File.createTempFile("unitTest", "GolemURLStreamHandlerFactory");
        URL fileUrl = FileHelper.createFileURL(tmpFile);
        URLConnection con = fileUrl.openConnection();
        assertEquals(FileURLConnection.class, con.getClass());
        assertTrue(con.getDoInput());
        assertTrue(con.getDoOutput());
        
        con.connect();
        InputStream inputStream = con.getInputStream();
        OutputStream outputStream = con.getOutputStream();
        assertNotNull(inputStream);
        assertNotNull(outputStream);
    }
    /**
     * Test of addPackagePrefix method, of class GolemURLStreamHandlerFactory.
     */
    @Test
    public void testAddPackagePrefix() {
        String prefix = "";
        GolemURLStreamHandlerFactory instance = new GolemURLStreamHandlerFactory();
        boolean result = instance.addPackagePrefix(prefix);
        assertFalse(result);

        prefix = "cz.a_d.automation.testClasses.protocol";
        result = instance.addPackagePrefix(prefix);
        assertTrue(result);

        URLStreamHandler handler = instance.createURLStreamHandler("file");
        assertNotNull(handler);
        assertTrue(handler instanceof cz.a_d.automation.testClasses.protocol.file.Handler);

        instance.removePackagePrefix(prefix);
    }

    /**
     * Test of removePackagePrefix method, of class GolemURLStreamHandlerFactory.
     */
    @Test
    public void testRemovePackagePrefix() {
        String prefix = "";
        GolemURLStreamHandlerFactory instance = new GolemURLStreamHandlerFactory();
        boolean result = instance.removePackagePrefix(prefix);
        assertFalse(result);

        prefix = "cz.a_d.automation.testClasses.protocol";
        result = instance.removePackagePrefix(prefix);
        assertFalse(result);

        result = instance.addPackagePrefix(prefix);
        assertTrue(result);

        result = instance.removePackagePrefix(prefix);
        assertTrue(result);
    }

    /**
     * Test of getLoadedProtocol method, of class GolemURLStreamHandlerFactory.
     */
    @Test
    public void testGetLoadedProtocol() {
        GolemURLStreamHandlerFactory instance = new GolemURLStreamHandlerFactory();
        Set<String> result = instance.getLoadedProtocol();
        assertNotNull(result);
        assertTrue(result.isEmpty());

        instance.createURLStreamHandler("file");
        result = instance.getLoadedProtocol();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Test of putProtocolHandler method, of class GolemURLStreamHandlerFactory.
     */
    @Test
    public void testPutProtocolHandler() {
        String protocol = "";
        Class<? extends URLStreamHandler> handler = null;
        GolemURLStreamHandlerFactory instance = new GolemURLStreamHandlerFactory();
        boolean result = instance.putProtocolHandler(protocol, handler);
        assertFalse(result);

        protocol = "file";
        result = instance.putProtocolHandler(protocol, handler);
        assertFalse(result);
        
        handler = cz.a_d.automation.testClasses.protocol.file.Handler.class;
        result = instance.putProtocolHandler("", handler);
        assertFalse(result);
        
        result = instance.putProtocolHandler(protocol, handler);
        assertTrue(result);        
    }

    /**
     * Test of getClass internal method, of class GolemURLStreamHandlerFactory. This test omnit testing agains empty and null pointer
     * values. It is resposibility of developer use this method by proper way.
     *
     * Test cover instancing class which is not extending URLStreamHandler and one standard class provided by sun and one class provided by
     * golem.
     */
    @Test
    public void testGetClass() {
        String className = "java.lang.Integer";
        GolemURLStreamHandlerFactory instance = new GolemURLStreamHandlerFactory();
        Class<? extends URLStreamHandler> result = instance.getClass(className);
        assertNull(result);

        className = "cz.a_d.automation.golem.protocol.file.Handler";
        Class<? extends URLStreamHandler> expResult = Handler.class;
        result = instance.getClass(className);
        assertSame(expResult, result);

        className = "sun.net.www.protocol.file.Handler";
        expResult = sun.net.www.protocol.file.Handler.class;
        result = instance.getClass(className);
        assertSame(expResult, result);
    }

}
