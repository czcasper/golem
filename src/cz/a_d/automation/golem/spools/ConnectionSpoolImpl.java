/*
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.spools.ConnectionSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleConnectionKey;
import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public class ConnectionSpoolImpl<A> extends AbstractSpoolImpl<A, ConnectionKey<?>, Connection> implements ConnectionSpool<A> {

    private static final long serialVersionUID = 1L;
    protected final static ConnectionSpool<Object> global = new ConnectionSpoolImpl<>();

    public ConnectionSpoolImpl() {
        super();
    }

    public ConnectionSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ConnectionSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    public ConnectionSpoolImpl(Map<? extends ConnectionKey<?>, ? extends Connection> m) {
        super(m);
    }

    public ConnectionSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public static ConnectionSpool<Object> getGlobal() {
        return global;
    }

    @Override
    public ConnectionSpool<A> newInstance() {
        return new ConnectionSpoolImpl<>();
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

    @Override
    protected boolean validateFieldType(Field f) throws IllegalAccessException {
        boolean retValue = true;
        if (f != null) {
            ActionFieldProxyType type = ActionFieldProxyType.getType(f);
            if ((type != null) && (type == ActionFieldProxyType.Connections)) {
                retValue = true;
            } else {
                String usedType="null";
                if(type!=null){
                    usedType =type.toString();
                }
                throw new IllegalAccessException("Parameter spool doesn''t supports field annotated by type:" + usedType);
            }
        }
        return retValue;
    }
}
