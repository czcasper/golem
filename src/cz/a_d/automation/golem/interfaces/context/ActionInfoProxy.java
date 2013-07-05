/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.context;

import cz.a_d.automation.golem.interfaces.spools.AbstractSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.enums.ActionMethodProxyType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 *
 * @author maslu02
 */
public interface ActionInfoProxy {

    public boolean addActionInfoProxy(ActionInfoProxy other);

    public boolean addField(ActionFieldProxyType type, Collection<Field> fields);

    public boolean addMethod(ActionMethodProxyType type, Collection<Method> methods);

    public List<Field> getField(ActionFieldProxyType type);

    public Field getField(String name);

    public SortedSet<Method> getMethod(ActionMethodProxyType type);

    public Map<ActionFieldProxyType, List<Field>> getFields();

    public Map<ActionMethodProxyType, SortedSet<Method>> getMethods();

    public Map<String, Field> getFieldNames();

    public <A> boolean loadAction(Class<?> action, AbstractSpool<A, ActionInfoKey<Class<?>>, ActionInfoProxy> loaded);

    public boolean isEmpty();
    
    public boolean isValid();

    public void clear();

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);
}
