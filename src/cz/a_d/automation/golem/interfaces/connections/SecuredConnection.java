/*
 */
package cz.a_d.automation.golem.interfaces.connections;

/**
 * Interface define operations common for authentications of connections.
 *
 * @author casper
 */
public interface SecuredConnection {

    /**
     * Authenticate connection with provided credentials.
     *
     * @param userName used for connection authentication.Must be different from null.
     * @param password used for connection authentication.Must be different from null.
     *
     * @return true in case when authentication has been successful, otherwise false.
     */
    public boolean login(String userName, String password);

    /**
     * Logging off connection from resource.
     *
     * @return true in case when log off has been successful, otherwise false.
     */
    public boolean logout();
}
