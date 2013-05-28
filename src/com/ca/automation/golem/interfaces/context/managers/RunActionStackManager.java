/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.interfaces.context.managers;

import com.ca.automation.golem.interfaces.context.RunActionStack;
import java.util.List;

/**
 *
 * @author maslu02
 */
public interface RunActionStackManager <T,K,V> extends ContextManager<T, RunActionStack<T>,K,V> {
    
    @Override
    public RunActionStack<T> getCurrent();
    
    public List<RunActionStack<T>> getActive();    
}
