/*
 */
package cz.a_d.automation.golem.interfaces;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;

/**
 * Interface represent factory for managing connection with resources based on resource URI. Factory provides method to create connection
 * from URI controlled by Golem.
 *
 * @author casper
 */
public interface ConnectionFactory {

    /**
     * Creating initialized instance of connection object with implementation of interface expected by Golem. This connection is referring
     * to resource defined by URI.
     *
     * @param uri   reference to resource in format of URI. To successfully initialize connection cannot be null.
     * @param proxy the Proxy through which this connection will be made. If direct connection is desired, Proxy.NO_PROXY should be
     *              specified.
     * @return initialized instance of connection to resource in case when there is no problem with connecting to resource, otherwise null.
     *
     * @throws IllegalArgumentException      will be thrown if proxy is null, or proxy has the wrong type, or if this URL is not absolute.
     *
     * @throws MalformedURLException         If a protocol handler for the URL could not be found, or if some other error occurred while
     *                                       constructing the URL.
     * @throws IOException                   if an I/O exception occurs.
     *
     * @throws SecurityException             if a security manager is present and the caller doesn't have permission to connect to the
     *                                       proxy.
     *
     * @throws UnsupportedOperationException if the subclass that implements the protocol handler doesn't support this method.
     */
    public Connection open(URI uri, Proxy proxy) throws MalformedURLException, IOException;

    /**
     * Creating initialized instance of connection object with implementation of interface expected by Golem. This connection is referring
     * to resource defined by URL.
     *
     * @param url   reference to resource in format of URL. To successfully initialize connection cannot be null.
     * @param proxy the Proxy through which this connection will be made. If direct connection is desired, Proxy.NO_PROXY should be
     *              specified.
     * @return initialized instance of connection to resource in case when there is no problem with connecting to resource, otherwise null.
     *
     * @throws IllegalArgumentException      will be thrown if proxy is null, or proxy has the wrong type.
     *
     * @throws IOException                   if an I/O exception occurs.
     *
     * @throws SecurityException             if a security manager is present and the caller doesn't have permission to connect to the
     *                                       proxy.
     *
     * @throws UnsupportedOperationException if the subclass that implements the protocol handler doesn't support this method.
     */
    public Connection open(URL url, Proxy proxy) throws IOException;

    /**
     * Creating initialized instance of connection object with implementation of interface expected by Golem. This connection is referring
     * to resource defined by URI.
     *
     * @param uri reference to resource in format of URI. To successfully initialize connection cannot be null.
     * @return initialized instance of connection to resource in case when there is no problem with connecting to resource, otherwise null.
     *
     * @throws IllegalArgumentException      will be thrown if this URL is not absolute.
     *
     * @throws MalformedURLException         If a protocol handler for the URL could not be found, or if some other error occurred while
     *                                       constructing the URL.
     * @throws IOException                   if an I/O exception occurs.
     *
     * @throws SecurityException             if a security manager is present and the caller doesn't have permission to connect to the
     *                                       proxy.
     *
     * @throws UnsupportedOperationException if the subclass that implements the protocol handler doesn't support this method.
     */
    public Connection open(URI uri) throws MalformedURLException, IOException;

    /**
     * Creating initialized instance of connection object with implementation of interface expected by Golem. This connection is referring
     * to resource defined by URL.
     *
     * @param url reference to resource in format of URL. To successfully initialize connection cannot be null.
     * @return initialized instance of connection to resource in case when there is no problem with connecting to resource, otherwise null.
     *
     * @throws IllegalArgumentException      will be thrown if this URL is not absolute.
     *
     * @throws IOException                   if an I/O exception occurs.
     *
     * @throws SecurityException             if a security manager is present and the caller doesn't have permission to connect to the
     *                                       proxy.
     *
     * @throws UnsupportedOperationException if the subclass that implements the protocol handler doesn't support this method.
     */
    public Connection open(URL url) throws IOException;

}
