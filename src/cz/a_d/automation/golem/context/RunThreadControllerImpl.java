/*
 */
package cz.a_d.automation.golem.context;

import java.util.List;
import java.util.Map;

/**
 *
 * @author casper
 * @param <T>
 */
public class RunThreadControllerImpl<T,V> {

    protected T startAction;
    protected T endAction;
    
    protected int count;
    
    protected List<T> steps;
    
    protected List<Map<String,V>> parameters;
    
    
}
