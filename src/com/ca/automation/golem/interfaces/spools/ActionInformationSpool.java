/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.spools;

import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.interfaces.spools.keys.ActionInfoKey;
import java.util.List;

/**
 *
 * @author maslu02
 */
public interface ActionInformationSpool<A> extends AbstractSpool<A, ActionInfoKey<Class<?>>, ActionInfoProxy> {

    public boolean isValidAction(Object action);

    public boolean isValidAction(Class<?> action);

    public <V> ActionStream<A, V> createNewFromObject(List<A> actions);

    public <V> ActionStream<A, V> createNewFromClasses(List<Class<?>> actions);

    @Override
    public ActionInfoProxy get(Object key);
}
