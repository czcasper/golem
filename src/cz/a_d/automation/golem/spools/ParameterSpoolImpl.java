/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ParameterKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleParameterKey;
import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public class ParameterSpoolImpl<A, V> extends AbstractSpoolImpl<A, ParameterKey<?>, V> implements ParameterSpool<A, V> {

    private static final long serialVersionUID = 1L;
    protected final static ParameterSpool<Object, Object> global = new ParameterSpoolImpl<>();

    public ParameterSpoolImpl() {
        super();
    }

    public ParameterSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ParameterSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    public ParameterSpoolImpl(Map<? extends ParameterKey<?>, ? extends V> m) {
        super(m);
    }

    public ParameterSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public static ParameterSpool<Object, Object> getGlobal() {
        return global;
    }

    @Override
    public ParameterSpool<A, V> newInstance() {
        return new ParameterSpoolImpl<>();
    }

    @Override
    protected ParameterKey createKey(String key) {
        return new SimpleParameterKey(key);
    }

    @Override
    protected <P> boolean instancOfKey(P value) {
        return (value instanceof ParameterKey);
    }

    @Override
    protected ParameterKey initProxyKey() {
        return new SimpleParameterKey(null);
    }

    @Override
    protected boolean validateFieldType(Field f) throws IllegalAccessException {
        boolean retValue = true;
        if (f != null) {
            ActionFieldProxyType type = ActionFieldProxyType.getType(f);
            if ((type != null) && ((type == ActionFieldProxyType.Contexts) || (type == ActionFieldProxyType.Parameters))) {
                retValue = true;
            } else {
                String usedType = "null";
                if(type!=null){
                    usedType = type.toString();
                }
                throw new IllegalAccessException("Parameter spool doesn''t supports field annotated by type:" + usedType);
            }
        }
        return retValue;
    }
}
