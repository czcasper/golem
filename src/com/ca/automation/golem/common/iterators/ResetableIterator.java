/*
 */
package com.ca.automation.golem.common.iterators;

import java.util.Iterator;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public class ResetableIterator<T> implements Iterator<T> {
    
    /**
     *
     */
    protected Iterator<T> it;

    /**
     *
     * @param it
     */
    public ResetableIterator(Iterator<T> it) {
        if(it == null){
            throw new IllegalStateException("Internal iterator is null");
        }
        this.it = it;
    }    

    /**
     * This method allows change iterator used by this class.
     * 
     * @param it new internal iterator for class must be diferent from null
     * 
     */
    public void setIt(Iterator<T> it) {
        if(it == null){
            throw new IllegalStateException("Internal iterator iterator can't be initialized by null value");
        }
        this.it = it;
    }

    /**
     * Method for accessing internal class iterator.
     * 
     * @return internal iterator.
     */
    public Iterator<T> getIt() {
        return it;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        return it.next();
    }

    @Override
    public void remove() {
        it.remove();
    }

}
