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
