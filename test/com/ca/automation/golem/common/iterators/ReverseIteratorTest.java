/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.common.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author maslu02
 */
public class ReverseIteratorTest {
    
    private List<Object> steps;
    @Rule
    public ExpectedException testException = ExpectedException.none();
    
    public ReverseIteratorTest() {
        steps = new ArrayList<Object>();
        for(int i=0;i<10;i++){
            steps.add(i);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class ReverseIterator.
     */
    @Test
    public void testHasNext() {
        ReverseIterator instance = new ReverseIterator(null);

        boolean result = instance.hasNext();
        assertFalse(result);
        
        instance = new ReverseIterator(steps);
        result = instance.hasNext();
        assertTrue(result);
        
        int index = steps.size()-1;
        for(Object o : instance){
            assertSame(steps.get(index--), o);
        }
        result = instance.hasNext();
        assertFalse(result);
    }

    /**
     * Test of next method, of class ReverseIterator.
     */
    @Test
    public void testNext() {
        ReverseIterator instance = new ReverseIterator(null);
        testException.expect(NoSuchElementException.class);
        testException.expectMessage("Reverse iterator wasn't initialize by list.");        
        instance.next();
    }

    /**
     * Test of remove method, of class ReverseIterator.
     */
    @Test
    public void testRemove() {
        ReverseIterator instance = new ReverseIterator(null);
        testException.expect(IllegalStateException.class);
        testException.expectMessage("Reverse iterator wasn't initialize by list.");        
        instance.remove();
    }

    /**
     * Test of remove method, of class ReverseIterator.
     */
    @Test
    public void testRemove2() {
        List<Object> testData = new ArrayList<Object>();
        
        
        for(int i=0;i<1000;i++){
            testData.add(i);
        }
        
        ReverseIterator instance = new ReverseIterator(testData);
        
        double rand;
        int index = testData.size()-1;
        while(instance.hasNext()){
           assertSame(testData.get(index--), instance.next());            
           instance.remove();
        }
        assertEquals(-1,index);
    }

    /**
     * Test of iterator method, of class ReverseIterator.
     */
    @Test
    public void testIterator() {
        ReverseIterator instance = new ReverseIterator(null);
        Iterator result = instance.iterator();
        assertNotNull(result);
        assertFalse(result.hasNext());
        
        instance = new ReverseIterator(steps);
        int index = steps.size()-1;
        for(Object o : instance){
            assertSame(steps.get(index--),o);
        }
        assertEquals(-1, index);
    }
}
