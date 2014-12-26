/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunCondition;
import java.util.List;

/**
 *
 * @author casper
 */
public interface RunCondManager<T, C extends Object, V> extends ContextManager<T, RunCondition<T, C>, V> {

    public boolean setup(T action, C expect, long counter);

    @Override
    public RunCondition<T, C> getCurrent();

    public List<RunCondition<T, C>> getActive();
}
