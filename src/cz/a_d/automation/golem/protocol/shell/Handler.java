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
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Implementation of interaction with local terminal through URLconnection. Supports selecting used bash program by path in URL. Server name
 * determine used shell program. In case when there is localhost defined, shell will be detected automatically. Path from URL is used to
 * specify working directory where shell will be executed in case when is not specified working directory where java program has been
 * executed will be used.
 *
 * @author casper
 */
public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        URLConnection retValue = null;
        if(u!=null){
            String host = u.getHost();
            if(host.equalsIgnoreCase("localhost")){
                host= null;
            }else{
                host=File.separator+host.replace('.', File.separatorChar);
            }
            retValue = new ShellURLConnection(u, host);
        }
        return retValue;
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        return this.openConnection(u);
    }

}
