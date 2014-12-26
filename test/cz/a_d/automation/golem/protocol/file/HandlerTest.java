/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.protocol.file;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author casper
 */
public class HandlerTest {

    public HandlerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of openConnection method, of class Handler.
     */
    @Test
    public void testOpenConnection() throws Exception {
        URL u = null;
        Handler instance = new Handler();
        URLConnection result = instance.openConnection(u);
        assertNull(result);

        File f = File.createTempFile("junit", "FileURLStreamHandlerTest#openConnection");
        u = FileHelper.createFileURL(f);
        result = instance.openConnection(u);
        assertNotNull(result);
        
        f.delete();
    }

}
