/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunActionStack;
import java.util.List;

/**
 *
 * @author maslu02
 */
public interface RunActionStackManager<T, V> extends ContextManager<T, RunActionStack<T>, V> {

    @Override
    public RunActionStack<T> getCurrent();

    public List<RunActionStack<T>> getActive();
}
