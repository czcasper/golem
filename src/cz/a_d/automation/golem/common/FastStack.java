/*
 */
package cz.a_d.automation.golem.common;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This class is replacement for Stack which slow because has protection for 
 * save multi-thread access to data. It is implemented like extension for 
 * LinkedList. This extension has just more obvious name and implement methods 
 * specific for stack by reusing LinkedList methods.
 * 
 * @author maslu02
 */
public class FastStack<E> extends LinkedList<E> {
    private static final long serialVersionUID = 1L;

    public FastStack() {
        super();
    }

    public FastStack(Collection<? extends E> c) {
        super(c);
    }
 
    /**
     * This method allows access to object on top of stack, object stay in stack.
     * 
     * @return Object on the top of stack
     */
    @Override
    public E peek(){
        return super.peek();
    }
    
    /**
     * Method inserting object into stack on first position.
     * @param item 
     */
    
    @Override
    public void push(E item){
        super.push(item);
    }
    
    /**
     * Method return and remove first object in stack.
     * 
     * @return first object in stack before method call.
     */
    @Override
    public E pop(){
        return super.pop();
    }

}
