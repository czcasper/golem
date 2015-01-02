package cz.a_d.automation.golem.protocol;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * URL stream handler faktory implementation for Golem. This implementation provides more complex features for searching protocol
 * implementation.
 *
 * @author casper
 */
public class GolemURLStreamHandlerFactory implements URLStreamHandlerFactory {

    /**
     * Default path used by original sun library and extended by package where golem store own custom implementation of protocol.
     */
    private static final String[] PROTOCOL_PACKAGE_PREFIX = {"cz.a_d.automation.golem.protocol", "sun.net.www.protocol"};

    /**
     * Default instance of factory used by golem for overwriting system one. This instance allows easy access to additional features of
     * golem protocol factory.
     */
    protected static final GolemURLStreamHandlerFactory defaultInstance = new GolemURLStreamHandlerFactory();

    /**
     * Map of loaded URLHandler classes for specific protocols.
     */
    protected Map<String, Class<? extends URLStreamHandler>> handlers = new HashMap<>();

    /**
     * Set of prefixes used for searching protocol handlers.
     */
    protected LinkedList<String> packagePrefixs = new LinkedList<>();

    /**
     * The property which specifies the package prefix list to be scanned for protocol handlers. The value of this property (if any) should
     * be a vertical bar delimited list of package names to search through for a protocol handler to load. The policy of this class is that
     * all protocol handlers will be in a class called <protocolname>.Handler, and each package in the list is examined in turn for a
     * matching handler. If none are found (or the property is not specified), the default package prefix, sun.net.www.protocol, is used.
     * The search proceeds from the first package in the list to the last and stops when a match is found.
     */
    private static final String protocolPathProp = "java.protocol.handler.pkgs";

    /**
     * Overwrite default URL stream handler during class load.
     */
    static {
        URL.setURLStreamHandlerFactory(defaultInstance);
    }

    /**
     * Construct instance of factory and load infromation from system defined properties.
     *
     */
    protected GolemURLStreamHandlerFactory() {
        String packagePrefixList = System.getProperty(protocolPathProp);
        if ((packagePrefixList != null) && (!packagePrefixList.isEmpty())) {
            StringTokenizer packagePrefixIter = new StringTokenizer(packagePrefixList, "|");
            while (packagePrefixIter.hasMoreTokens()) {
                String name = packagePrefixIter.nextToken().trim();
                if (!packagePrefixs.contains(name)) {
                    packagePrefixs.add(name);
                }
            }
        }
        for (String prefix : PROTOCOL_PACKAGE_PREFIX) {
            if (!packagePrefixs.contains(prefix)) {
                packagePrefixs.add(prefix);
            }
        }
    }

    /**
     * Method which allows access instance URL factory used by golem to translate URL.
     *
     * @return default instance of URL stream factory.
     */
    public static GolemURLStreamHandlerFactory getDefaultInstance() {
        return defaultInstance;
    }

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        URLStreamHandler retValue = null;
        boolean findFlag = false;
        if (!handlers.containsKey(protocol)) {
            findFlag = findProtocolHandler(protocol);
        }

        if ((findFlag) || handlers.containsKey(protocol)) {
            try {
                retValue = handlers.get(protocol).newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(GolemURLStreamHandlerFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retValue;
    }

    /**
     * Method allow add package prefix into first place of packages prefixes list.
     *
     * @param prefix must be non empty string, with valid package name.
     * @return true in case when prefix is succesfully inserted.
     */
    public boolean addPackagePrefix(String prefix) {
        boolean retValue = false;
        if ((prefix != null) && (!prefix.isEmpty()) && (!packagePrefixs.contains(prefix))) {
            packagePrefixs.addFirst(prefix);
            handlers.clear();
            retValue = true;
        }
        return retValue;
    }

    /**
     * Method allow remove package prefix from factory list of prefixes used during searching for protocol implementation.
     *
     * @param prefix prefix of package where are stored protocol handlers .
     *
     * @return true in case when prefix is succesfully removed.
     */
    public boolean removePackagePrefix(String prefix) {
        boolean retValue = false;
        if (packagePrefixs.contains(prefix)) {
            retValue = packagePrefixs.remove(prefix);
            handlers.clear();
        }
        return retValue;
    }

    /**
     * Allow access to curently loaded protocol by this factory.
     *
     * @return set of protocol names currently loaded in this facotry.
     */
    public Set<String> getLoadedProtocol() {
        return handlers.keySet();
    }

    /**
     * Method which allows put handler directly into factory.
     *
     * @param protocol protocol name must be different from null end empty.
     * @param handler  class must be instance of URLStreamHandler and different from null
     * @return true in case when protocol handler and protocol handler is correctly inserted into factory, otherwise false.
     */
    public boolean putProtocolHandler(String protocol, Class<? extends URLStreamHandler> handler) {
        boolean retValue = false;
        if ((protocol != null) && (!protocol.isEmpty()) && (handler != null)) {
            handlers.put(protocol, handler);
            retValue = true;
        }
        return retValue;
    }

    /**
     * Finding protocol handler class in specific prefixes managed by this factory.
     *
     * @param protocol protocol name. It is used like part of class name in searching for protocol handler.
     * @return true in case when protocol handler has been found and loaded into factory.
     */
    protected boolean findProtocolHandler(String protocol) {
        boolean retValue = false;
        if (!handlers.containsKey(protocol)) {
            for (String prefix : packagePrefixs) {
                String handlerFullName = prefix + "." + protocol + ".Handler";
                Class<? extends URLStreamHandler> handler = getClass(handlerFullName);
                if (handler != null) {
                    handlers.put(protocol, handler);
                    retValue = true;
                    break;
                }
            }
        }
        return retValue;
    }

    /**
     * Method will create from class string name represenataion and validate if this class implements URLStreamHandler.
     *
     * @param className must be valid class name which is implementing URL class handler.
     * @return in case when className represent valid URLStreamHandler return class object otherwise return null.
     */
    @SuppressWarnings("unchecked")
    protected Class<? extends URLStreamHandler> getClass(String className) {
        Class<? extends URLStreamHandler> retValue = null;
        try {
            Class<?> handler = Class.forName(className);
            if (URLStreamHandler.class.isAssignableFrom(handler)) {
                retValue = (Class<? extends URLStreamHandler>) handler;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GolemURLStreamHandlerFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retValue;
    }
}
