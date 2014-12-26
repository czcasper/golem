/*
 */
package cz.a_d.automation.golem.protocol.file;

import cz.a_d.automation.golem.io.FileAccessModificator;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLStreamHandler;

/**
 *
 * @author casper
 */
public class Handler extends URLStreamHandler {

    protected sun.net.www.protocol.file.Handler defaultHandler = new sun.net.www.protocol.file.Handler();
    protected String urlEncoding = "UTF-8";

    @Override
    public synchronized URLConnection openConnection(URL u) throws IOException {
        URLConnection retValue = null;
        if (u != null) {
            String host = u.getHost();
            if ((host == null) || (host.isEmpty()) || (host.equals("~")) || (host.compareToIgnoreCase("localhost") == 0)) {
                String query = u.getQuery();
                if ((query != null) && (!query.isEmpty())) {
                    String decoded = URLDecoder.decode(query, urlEncoding);
                    FileAccessModificator[] modi = FileAccessModificator.fromQuery(decoded);
                    String path = URLDecoder.decode(u.getPath(), urlEncoding);
                    retValue = new FileURLConnection(u, new File(path), modi);
                } else {
                    retValue = defaultHandler.openConnection(u);
                }
            }
        }
        return retValue;
    }

    @Override
    public URLConnection openConnection(URL u, Proxy p) throws IOException {
        return this.openConnection(u);
    }
    
    
}
