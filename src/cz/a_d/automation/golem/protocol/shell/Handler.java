/*
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
