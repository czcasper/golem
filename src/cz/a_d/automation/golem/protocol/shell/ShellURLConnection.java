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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Implementation of wrapper representing connection to runtime environment represented by shell program.
 *
 * @author casper
 */
public class ShellURLConnection extends URLConnection {

    /**
     * Builder is stored to postpone execution of thread with shell to method connect.
     */
    protected ProcessBuilder proces;
    /**
     * Process represents thread with running shell in runtime environment.
     */
    protected Process proc;

    /**
     * Create new instance of implementation of connection to local shell environment. Path from url is used for working directory. This
     * path is absolute and must be existing directory, in other cases working directory from java program has been used.
     *
     * @param url   instance of url pointing to shell, path used to setup working directory.
     * @param shell path to command which represents shell requested by connection. Could be null in this case shell is detected
     *              automatically.
     */
    public ShellURLConnection(URL url, String shell) {
        super(url);
        if (shell == null || shell.isEmpty()) {
            SystemDefaultShell shellEnum = SystemDefaultShell.getShell(System.getProperty("os.name"));
            if (shellEnum != null) {
                shell = shellEnum.getShell();
            }
        }
        proces = new ProcessBuilder();
        proces = proces.command(shell);
        proces.redirectErrorStream(true);
        String path = url.getPath();
        if (path != null && (!path.isEmpty())) {
            File workDir = new File(path);
            if (workDir.isDirectory()) {
                proces = proces.directory(workDir);
            }
        }
    }

    @Override
    public void connect() throws IOException {
        proc = proces.start();
    }

    @Override
    public boolean getDoOutput() {
        return proc.isAlive();
    }

    @Override
    public boolean getDoInput() {
        return proc.isAlive();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return proc.getOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return proc.getInputStream();
    }

}
