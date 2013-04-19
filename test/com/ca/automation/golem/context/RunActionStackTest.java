/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author maslu02
 */
public class RunActionStackTest {

    @Rule
    public ExpectedException testException = ExpectedException.none();
    
    public RunActionStackTest() {
    }

    /**
     * Test of hasNext method, of class RunActionStack.
     */
    @Test
    public void testHasNext() {
        RunActionStack<Integer> instance = new RunActionStack<Integer>();
        boolean result = instance.hasNext();
        assertFalse(result);
        
        Integer[] testData = {1,2,3,4,5};
        boolean setupStack = instance.setupStack(1,testData);
        assertTrue(setupStack);
        
        List<Integer> tmp = new ArrayList<Integer>(Arrays.asList(testData));
        Collections.reverse(tmp);
        Iterator<Integer> it = tmp.iterator();
        for(int i=0;i<testData.length;i++){
            result = instance.hasNext();
            assertTrue(result);
            Integer next = instance.next();
            assertEquals(it.next(), next);
        }
        assertFalse(instance.hasNext());        
    }

    /**
     * Test of next method, of class RunActionStack.
     */
    @Test
    public void testNext() {
        RunActionStack<Integer> instance = new RunActionStack<Integer>();
        Integer result = instance.next();
        assertNull(result);
        
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int i=0;i<7;i++) {
            actions.add(i);
        }
        Integer[] tmp = new Integer[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setupStack(result, tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);
        
        Iterator<Integer> it = actions.iterator();
        while(instance.hasNext()){
            result = instance.next();
            assertEquals(it.next(), result);
        }
        assertTrue(instance.isEmpty());        
    }

    /**
     * Test of remove method, of class RunActionStack.
     */
    @Test
    public void testRemove() {
        RunActionStack<Integer> instance = new RunActionStack<Integer>();
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int i=0;i<7;i++) {
            actions.add(i);
        }
        Integer[] tmp = new Integer[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setupStack(1, tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);
        
        Iterator<Integer> it = actions.iterator();
        while(instance.hasNext()){
            Integer result = instance.next();
            assertEquals(it.next(), result);
            instance.remove();
            if(it.hasNext()) {
                it.next();
            }
        }
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of isEmpty method, of class RunActionStack.
     */
    @Test
    public void testIsEmpty() {
        RunActionStack instance = new RunActionStack();
        boolean result = instance.isEmpty();
        assertTrue(result);
    }

    /**
     * Test of peek method, of class RunActionStack.
     */
    @Test
    public void testPeek() {
        RunActionStack<Integer> instance = new RunActionStack<Integer>();
        Integer result = instance.peek();
        assertNull(result);
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int i=0;i<7;i++) {
            actions.add(i);
        }
        Integer[] tmp = new Integer[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setupStack(1, tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);
        
        Iterator<Integer> it = actions.iterator();
        while(instance.hasNext()){
            Integer valid = it.next();
            result = instance.peek();
            assertEquals(valid, result);
            result = instance.next();
            assertEquals(valid, result);
        }
        assertTrue(instance.isEmpty());
        
        
    }

    /**
     * Test of pop method, of class RunActionStack.
     */
    @Test
    public void testPop() {
        RunActionStack instance = new RunActionStack();
        testException.expect(NoSuchElementException.class);
        Object result = instance.pop();
    }

    /**
     * Test of push method, of class RunActionStack.
     */
    @Test
    public void testPush() {
        Integer item = null;
        RunActionStack<Integer> instance = new RunActionStack<Integer>();
        boolean result = instance.push(item);
        assertFalse(result);
        
        item = Integer.MIN_VALUE;
        result = instance.push(item);
        assertTrue(result);
    }

    /**
     * Test of setupStack method, of class RunActionStack.
     */
    @Test
    public void testSetupStack() {
        Integer action = null;
        Integer[] actions = null;
        RunActionStack<Integer> instance = new RunActionStack<Integer>();
        boolean result = instance.setupStack(action, actions);
        assertFalse(result);
        
        action = Integer.MIN_VALUE;
        result = instance.setupStack(action, actions);
        assertFalse(result);
        
        actions = new Integer[0];
        result = instance.setupStack(action, actions);
        assertFalse(result);
        
        actions = new Integer[]{1,2,3,4,5,6};
        result = instance.setupStack(action, actions);
        assertTrue(result);
    }

    /**
     * Test of getAction method, of class RunActionStack.
     */
    @Test
    public void testGetAction() {
        RunActionStack<Integer> instance = new RunActionStack<Integer>();
        Integer result = instance.getAction();
        assertNull(result);
        
        boolean setup = instance.setupStack(Integer.MAX_VALUE, 1,2,3,4,5,6,7);
        assertTrue(setup);
        
        result = instance.getAction();
        assertEquals(Integer.MAX_VALUE,(Object)result);
    }

    /**
     * Test of clone method, of class RunActionStack.
     */
    @Test
    public void testClone() throws Exception {
        RunActionStack<Integer> instance = new RunActionStack<Integer>();

        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int i=0;i<7;i++) {
            actions.add(i);
        }
        Integer[] tmp = new Integer[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setupStack(1, tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);        
        
        RunActionStack<Integer> result = instance.clone();
        Iterator<Integer> it = actions.iterator();
        while(result.hasNext()){
            assertEquals(it.next(), result.next());
        }
        assertTrue(result.isEmpty());
        assertTrue(instance.hasNext());
        
        it = actions.iterator();
        while(instance.hasNext()){
            assertEquals(it.next(), instance.next());
        }
        assertTrue(instance.isEmpty());        
        
    }
}
