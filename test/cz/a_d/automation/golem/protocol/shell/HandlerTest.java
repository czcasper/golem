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
package cz.a_d.automation.golem.protocol.shell;

import cz.a_d.automation.golem.protocol.GolemURLStreamHandlerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author casper
 */
// TODO platform independent testing.
public class HandlerTest {
    
    protected String lsCommand;
    protected String rootPath;

    public HandlerTest() {
        GolemURLStreamHandlerFactory.getDefaultInstance();
        SystemDefaultShell shell = SystemDefaultShell.getShell(System.getProperty("os.name"));
        if(shell==SystemDefaultShell.Windows){
            lsCommand="dir";
            rootPath="C:/";
        }else{
            lsCommand="ls";
            rootPath="/";
        }
    }

    /**
     * Test of openConnection method, of class Handler.
     */
    @Test
    public void testOpenConnection_URL() throws Exception {
        URL u = null;
        Handler instance = new Handler();
        URLConnection result = instance.openConnection(u);
        assertNull(result);

        u = new URL("shell://localhost");
        result = instance.openConnection(u);
        assertNotNull(result);

        result.connect();
        assertTrue(result.getDoInput());
        assertTrue(result.getDoOutput());

        OutputStream outputStream = result.getOutputStream();
        BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(result.getInputStream()));
        assertFalse(brCleanUp.ready());

        outputStream.write((lsCommand+"\n").getBytes());
        outputStream.flush();

        String line;
        File testFoler = new File("./");
        Set<String> listFiles = new HashSet<>(Arrays.asList(testFoler.list()));
        
        while ((line = brCleanUp.readLine()) != null) {
            assertNotNull(line);
            assertFalse(line.isEmpty());
            assertTrue(listFiles.contains(line));
            if (!brCleanUp.ready()) {
                break;
            }
        }

        outputStream.write((lsCommand+"\n").getBytes());
        outputStream.flush();
        while ((line = brCleanUp.readLine()) != null) {
            assertNotNull(line);
            assertFalse(line.isEmpty());
            assertTrue(listFiles.contains(line));
            if (!brCleanUp.ready()) {
                break;
            }
        }

        brCleanUp.close();
        outputStream.close();
    }

    /**
     * Test of openConnection method, of class Handler.
     */
    @Test
    public void testOpenConnection_URL_Proxy() throws Exception {
        URL u = null;
        Proxy p = null;
        Handler instance = new Handler();
        URLConnection result = instance.openConnection(u, p);
        assertNull(result);
        
        u = new URL("shell://bin.bash/");
        result = instance.openConnection(u);
        File testFoler = new File(rootPath);
        Set<String> listFiles = new HashSet<>(Arrays.asList(testFoler.list()));
        
        assertNotNull(result);

        result.connect();
        assertTrue(result.getDoInput());
        assertTrue(result.getDoOutput());
        
        OutputStream outputStream = result.getOutputStream();
        BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(result.getInputStream()));
        assertFalse(brCleanUp.ready());

        outputStream.write((lsCommand+"\n").getBytes());
        outputStream.flush();

        String line;
        while ((line = brCleanUp.readLine()) != null) {
            assertNotNull(line);
            assertFalse(line.isEmpty());
            assertTrue(listFiles.contains(line));
            if (!brCleanUp.ready()) {
                break;
            }
        }
        
    }

}
