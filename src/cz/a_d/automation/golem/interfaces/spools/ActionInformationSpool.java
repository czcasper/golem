/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
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

    public ActionInfoProxy getProxyFromObject(Object key);
}
