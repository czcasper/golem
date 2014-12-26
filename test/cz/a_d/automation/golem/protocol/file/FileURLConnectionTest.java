/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.protocol.file;

import cz.a_d.automation.golem.io.FileAccessModificator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author casper
 */
public class FileURLConnectionTest {

    /**
     * File storage used by test.
     */
    protected File file;
    /**
     * URL connected with file used by test.
     */
    protected URL fileURL;

    /**
     * Initialize temporarly file before every test method.
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        file = File.createTempFile("unitTest", "FileURLConnection");
        fileURL = file.toURI().toURL();
    }

    /**
     * Delete temporary file after every test method.
     */
    @After
    public void tearDown() {
        fileURL = null;
        file.delete();
    }

    /**
     * Validate if connection is opened with specified access to resource.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testConnect() throws Exception {
        FileURLConnection instance = new FileURLConnection(fileURL, file, new FileAccessModificator[]{FileAccessModificator.READ});
        instance.connect();
        assertTrue(instance.getDoInput());
        assertFalse(instance.getDoOutput());
        assertNotNull(instance.getInputStream());
        assertNull(instance.getOutputStream());

        instance = new FileURLConnection(fileURL, file, new FileAccessModificator[]{FileAccessModificator.WRITE});
        instance.connect();
        assertFalse(instance.getDoInput());
        assertTrue(instance.getDoOutput());
        assertNull(instance.getInputStream());
        assertNotNull(instance.getOutputStream());

        instance = new FileURLConnection(fileURL, file, new FileAccessModificator[]{FileAccessModificator.WRITE, FileAccessModificator.READ});
        instance.connect();
        assertTrue(instance.getDoInput());
        assertTrue(instance.getDoOutput());
        assertNotNull(instance.getInputStream());
        assertNotNull(instance.getOutputStream());
    }

    /**
     * Test functinality of output stream by using 
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetOutputStream() throws Exception {
        FileURLConnection instance = new FileURLConnection(fileURL, file, new FileAccessModificator[]{FileAccessModificator.WRITE, FileAccessModificator.READ});
        instance.connect();
        
        OutputStream result = instance.getOutputStream();
        assertNotNull(result);
        byte[] bytes = "hello world test".getBytes();
        result.write(bytes);
        
        for (int i = 0; i < bytes.length; i++) {
            bytes[i]=0;
        }
        InputStream inputStream = instance.getInputStream();
        byte[] test = "test".getBytes();
        result.write(test);
        result.flush();
        
        int read = inputStream.read(bytes);
        assertEquals(bytes.length, read);
        assertArrayEquals("hello world test".getBytes(), bytes);
        
        read = inputStream.read(bytes);
        assertEquals(test.length, read);
        assertArrayEquals("test".getBytes(), Arrays.copyOfRange(bytes, 0, read));
        
    }

    /**
     * Test of getInputStream method, of class FileURLConnection.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetInputStream() throws Exception {
        FileURLConnection instance = new FileURLConnection(fileURL, file, new FileAccessModificator[]{FileAccessModificator.WRITE, FileAccessModificator.READ});
        
        InputStream result = instance.getInputStream();
        assertNull(result);

        instance.connect();
        result = instance.getInputStream();
        assertNotNull(result);
        
        byte[] bytes = "hello world test".getBytes();
        int read = result.read(bytes);
        assertEquals(-1, read);
        
        OutputStream out = instance.getOutputStream();
        assertNotNull(out);
        out.write(bytes);
        out.flush();
        
        byte[] readBuffer= new byte[bytes.length];
        read = result.read(readBuffer);
        assertEquals(bytes.length, read);
        assertArrayEquals("hello world test".getBytes(), readBuffer);
    }

}
