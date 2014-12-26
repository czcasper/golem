/*
 */
package cz.a_d.automation.golem.context.connections;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.protocol.GolemURLStreamHandlerFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author casper
 */
public class GolemConnectionFactory {

    protected final static GolemConnectionFactory global = new GolemConnectionFactory();

    static {
        GolemURLStreamHandlerFactory.getDefaultInstance();
    }

    public static GolemConnectionFactory getGlobalFactory() {
        return global;
    }

    public static Connection open(URI uri, Proxy proxy) throws MalformedURLException, IOException {
        Connection retValue = null;
        if (uri != null) {
            retValue = open(uri.toURL(), proxy);
        }
        return retValue;
    }

    public static Connection open(URL url, Proxy proxy) throws IOException {
        Connection retValue = null;
        if (url != null) {
            URLConnection openConnection;
            if (proxy != null) {
                openConnection = url.openConnection(proxy);
            } else {
                openConnection = url.openConnection();
            }
            retValue = new ConnectionImpl(openConnection);
        }
        return retValue;
    }

    public static Connection open(URI uri) throws MalformedURLException, IOException {
        Connection retValue = null;
        if (uri != null) {
            retValue = open(uri.toURL());
        }
        return retValue;
    }

    public static Connection open(URL url) throws IOException {
        Connection retValue = null;
        if (url != null) {
            URLConnection urlCon = url.openConnection();
            retValue = new ConnectionImpl(urlCon);
            retValue.open();
        }
        return retValue;
    }
}
