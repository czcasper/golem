/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
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
     * Test of hasNext method, of class RunActionStackImpl.
     */
    @Test
    public void testHasNext() {
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();
        boolean result = instance.hasNext();
        assertFalse(result);
        
        Integer[] testData = {1,2,3,4,5};
        boolean setupStack = instance.setupStack(1,testData);
        assertTrue(setupStack);
        
        List<Integer> tmp = new ArrayList<>(Arrays.asList(testData));
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
     * Test of next method, of class RunActionStackImpl.
     */
    @Test
    public void testNext() {
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();
        Integer result = instance.next();
        assertNull(result);
        
        ArrayList<Integer> actions = new ArrayList<>();
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
     * Test of remove method, of class RunActionStackImpl.
     */
    @Test
    public void testRemove() {
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();
        ArrayList<Integer> actions = new ArrayList<>();
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
     * Test of isEmpty method, of class RunActionStackImpl.
     */
    @Test
    public void testIsEmpty() {
        RunActionStackImpl instance = new RunActionStackImpl();
        boolean result = instance.isEmpty();
        assertTrue(result);
    }

    /**
     * Test of peek method, of class RunActionStackImpl.
     */
    @Test
    public void testPeek() {
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();
        Integer result = instance.peek();
        assertNull(result);
        ArrayList<Integer> actions = new ArrayList<>();
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
     * Test of pop method, of class RunActionStackImpl.
     */
    @Test
    public void testPop() {
        RunActionStackImpl instance = new RunActionStackImpl();
        testException.expect(NoSuchElementException.class);
        Object result = instance.pop();
    }

    /**
     * Test of push method, of class RunActionStackImpl.
     */
    @Test
    public void testPush() {
        Integer item = null;
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();
        boolean result = instance.push(item);
        assertFalse(result);
        
        item = Integer.MIN_VALUE;
        result = instance.push(item);
        assertTrue(result);
    }

    /**
     * Test of setupStack method, of class RunActionStackImpl.
     */
    @Test
    public void testSetupStack() {
        Integer action = null;
        Integer[] actions = null;
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();
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
     * Test of getAction method, of class RunActionStackImpl.
     */
    @Test
    public void testGetAction() {
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();
        Integer result = instance.getAction();
        assertNull(result);
        
        boolean setup = instance.setupStack(Integer.MAX_VALUE, new Integer[]{1,2,3,4,5,6,7});
        assertTrue(setup);
        
        result = instance.getAction();
        assertEquals(Integer.MAX_VALUE,(Object)result);
    }

    /**
     * Test of clone method, of class RunActionStackImpl.
     */
    @Test
    public void testClone() throws Exception {
        RunActionStackImpl<Integer> instance = new RunActionStackImpl<>();

        ArrayList<Integer> actions = new ArrayList<>();
        for(int i=0;i<7;i++) {
            actions.add(i);
        }
        Integer[] tmp = new Integer[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setupStack(1, tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);        
        
        RunActionStackImpl<Integer> result = (RunActionStackImpl<Integer>) instance.clone();
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
