/*
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.connections.Connection;
import com.ca.automation.golem.interfaces.spools.ConnectionKey;
import com.ca.automation.golem.spools.keys.SimpleConnectionKey;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public class ConnectionSpoolImpl<A> extends AbstractSpoolImpl<A, ConnectionKey, Connection> {

    public ConnectionSpoolImpl() {
    }

    public ConnectionSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ConnectionSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    public ConnectionSpoolImpl(Map<? extends ConnectionKey, ? extends Connection> m) {
        super(m);
    }

    public ConnectionSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    @Override
    protected ConnectionKey createKey(String key) {
        return new SimpleConnectionKey(key);
    }

    @Override
    protected <P> boolean instancOfKey(P value) {
        return (value instanceof ConnectionKey);
    }

    @Override
    protected ConnectionKey initProxyKey() {
        return new SimpleConnectionKey(null);
    }


}
