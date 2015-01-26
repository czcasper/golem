/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ParameterKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleParameterKey;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Implementation of spool of action parameters used by runner to share parameters between actions.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 * @param <V> the type of value in spool.
 */
public class ParameterSpoolImpl<A, V> extends AbstractSpoolImpl<A, ParameterKey<?>, V> implements ParameterSpool<A, V> {

    private static final long serialVersionUID = 1L;
    /**
     * Global instance of spool used to share parameters across all runner thread.
     */
    protected final static ParameterSpool<Object, Object> global = new ParameterSpoolImpl<>();

    /**
     * Constructs an empty <tt>Parameter spool</tt> with capacity 16, load factor 0.75 and sorting values by based on amount of access to
     * value.
     */
    public ParameterSpoolImpl() {
        super();
    }

    /**
     * Constructs an empty <tt>Parameter spool</tt> with the specified initial capacity and load factor and sorting values by based on
     * amount of access to value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     */
    public ParameterSpoolImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructs an empty <tt>Parameter spool</tt> with the specified initial capacity and sorting values by based on amount of access to
     * value.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     */
    public ParameterSpoolImpl(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a new <tt>Parameter spool</tt> with the same mappings as the specified <tt>Map</tt>. The <tt>Spool</tt> is created with
     * default load factor (0.75) and an initial capacity sufficient to hold the mappings in the specified <tt>Map</tt>.
     *
     * @param m the map whose mappings are to be placed in this spool.
     */
    public ParameterSpoolImpl(Map<? extends ParameterKey<?>, ? extends V> m) {
        super(m);
    }

    /**
     * Constructs an empty <tt>Parameter spool</tt> instance with the specified initial capacity, load factor and ordering mode.
     *
     * @param initialCapacity initial capacity of spool, must be greater than zero.
     * @param loadFactor      the load factor.
     * @param accessOrder     the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
     */
    public ParameterSpoolImpl(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    /**
     * Getter for global instance of Parameter spool.
     *
     * @return global instance of parameter spool.
     */
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
                if (type != null) {
                    usedType = type.toString();
                }
                throw new IllegalAccessException("Parameter spool doesn''t supports field annotated by type:" + usedType);
            }
        }
        return retValue;
    }
}
