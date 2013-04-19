/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.common.iterators;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public class ReverseIterator<T> implements Iterator<T>, Iterable<T> {
    
    private ListIterator<T> iterator;

    /**
     *
     * @param list
     */
    public ReverseIterator(List<T> list) {
        if(list!=null) {
            this.iterator = list.listIterator(list.size());
        }
    }

    @Override
    public boolean hasNext() {
        if(iterator==null){
            return false;
        }
        return iterator.hasPrevious();
    }

    @Override
    public T next() {
        if (iterator == null) {
            throw new NoSuchElementException("Reverse iterator wasn't initialize by list.");
        }
        
        return iterator.previous();
    }

    @Override
    public void remove() {
        if (iterator == null) {
            throw new IllegalStateException("Reverse iterator wasn't initialize by list.");
        }
        
        iterator.remove();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
    
}
