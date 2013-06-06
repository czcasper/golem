/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.ActionInformationSpool;
import com.ca.automation.golem.interfaces.spools.keys.ActionInfoKey;
import com.ca.automation.golem.spools.actions.ActionInfoProxyImpl;
import com.ca.automation.golem.spools.enums.ActionMethodProxyType;
import com.ca.automation.golem.spools.keys.SimpleActionInfoKey;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

/**
 * This class is storage for
 *
 * @author maslu02
 */
@Singleton
@LocalBean
public class ActionInformationSpoolImpl<A> extends AbstractSpoolImpl<A, ActionInfoKey<Class<?>>, ActionInfoProxy> implements ActionInformationSpool<A> {

    protected Map<ActionMethodProxyType, Comparator<Method>> methodComparator = ActionInfoProxyImpl.createNewComparators();

    static {
        global = new ActionInformationSpoolImpl<Object>();
    }

    public static ActionInformationSpool<Object> getGlobal(){
        return (ActionInformationSpool<Object>) global;
    }
    @Override
    protected ActionInfoKey createKey(String key) {
        try {
            return new SimpleActionInfoKey(key);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ActionInformationSpoolImpl.class.getName()).log(Level.WARNING, null, ex);
        }
        return null;
    }

    @Override
    protected <P> boolean instancOfKey(P value) {
        return (value instanceof ActionInfoKey);
    }

    @Override
    protected ActionInfoKey initProxyKey() {
        return new SimpleActionInfoKey((Class<A>) null);
    }

    @Override
    public AbstractSpool<A, ActionInfoKey<Class<?>>, ActionInfoProxy> newInstance() {
        return new ActionInformationSpoolImpl<A>();
    }

    @Override
    public boolean isValidAction(Object action) {
        boolean retValue = false;
        if (action != null) {
            Class<?> testClass;
            if (action instanceof ActionInfoKey) {
                action = ((ActionInfoKey) action).get();
            }
            if (action instanceof Class) {
                testClass = (Class<?>) action;

            } else {
                testClass = action.getClass();
            }

            retValue = isValidAction(testClass);
        }
        return retValue;
    }

    
    @Override
    public boolean isValidAction(Class<?> action) {
        boolean retValue = false;
        if (action != null) {
            searchProxy.set(action);
            if (super.containsKey(searchProxy)) {
                retValue = true;
            } else {
                ActionInfoProxy info = new ActionInfoProxyImpl(methodComparator);
                if (info.loadAction(action, this)) {
                    ActionInfoKey tmpKey = new SimpleActionInfoKey(action.getClass());
                    super.put(tmpKey, info);
                    retValue = true;
                }
            }
        }
        return retValue;
    }

    @Override
    public boolean containsKey(Object key) {
        return isValidAction(key);
    }

    @Override
    public ActionInfoProxy get(String key) {
        ActionInfoProxy retValue = null;
        searchProxy.load(key);
        if (isValidAction(searchProxy)) {
            retValue = super.get(searchProxy);
        }
        return retValue;
    }

    @Override
    public ActionInfoProxy get(Object key) {
        ActionInfoProxy retValue = null;
        if (isValidAction(key)) {
            Class<?> tmpKey;
            if (key instanceof Class) {
                tmpKey = (Class<?>) key;
            } else {
                tmpKey = key.getClass();
            }
            searchProxy.set(tmpKey);
            retValue = super.get(searchProxy);
        }
        return retValue;
    }
}
