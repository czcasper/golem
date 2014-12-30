/*
 */
package cz.a_d.automation.golem.context.connections;

import cz.a_d.automation.golem.interfaces.ConnectionFactory;
import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.protocol.GolemURLStreamHandlerFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * Implementation of factory for managing connection with resources based on resource URI. Factory provides method to create connection from
 * URI controlled by Golem.
 *
 * @author casper
 */
public class GolemConnectionFactory implements ConnectionFactory {

    /**
     * Global instance of factory used for managing all connections currently used by actions from all processed action streams.
     */
    protected final static GolemConnectionFactory global = new GolemConnectionFactory();

    static {
        /**
         * Load GolemURLStreamHandlerFactory instance default URL stream handler factory provided by java implementation.
         */
        GolemURLStreamHandlerFactory.getDefaultInstance();
    }

    /**
     * Getter to access global instance of factory.
     *
     * @return global instance of factory for sharing between all processed action streams.
     */
    public static GolemConnectionFactory getGlobalFactory() {
        return global;
    }

    @Override
    public Connection open(URI uri, Proxy proxy) throws MalformedURLException, IOException {
        Connection retValue = null;
        if (uri != null) {
            retValue = open(uri.toURL(), proxy);
        }
        return retValue;
    }

    @Override
    public Connection open(URL url, Proxy proxy) throws IOException {
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

    @Override
    public Connection open(URI uri) throws MalformedURLException, IOException {
        Connection retValue = null;
        if (uri != null) {
            retValue = open(uri.toURL());
        }
        return retValue;
    }

    @Override
    public Connection open(URL url) throws IOException {
        Connection retValue = null;
        if (url != null) {
            URLConnection urlCon = url.openConnection();
            retValue = new ConnectionImpl(urlCon);
            retValue.open();
        }
        return retValue;
    }
}
