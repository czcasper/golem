/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.context;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author casper
 */
// TODO Refactoring: Validate functionality of this test and improve it to be more complex.
public class RunDelayIntervalImplTest {
    
    public RunDelayIntervalImplTest() {
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
     * Test of hasNext method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testHasNext() {
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();
        assertFalse(instance.hasNext());
        
        long actionCount = 10;
        long time = 10;
        boolean setupTimer = instance.setupTimer(Integer.SIZE, actionCount, time);
        assertTrue(setupTimer);
        
        long measured;
        for (long i = 0; i < actionCount; i++) {
            assertTrue(instance.hasNext());
            measured = System.currentTimeMillis();
            instance.next();
            measured = System.currentTimeMillis() - measured;
            if (measured < time) {
                fail("Timer not waited expected time");
            }            
        }
        assertFalse(instance.hasNext());
        assertEquals((Object) Integer.SIZE, instance.getAction());
        
        setupTimer = instance.setupTimer(Integer.SIZE, -1l, time);
        assertTrue(setupTimer);
        
        for (long i = 0; i < 5; i++) {
            assertTrue(instance.hasNext());
            measured = System.currentTimeMillis();
            instance.next();
            measured = System.currentTimeMillis() - measured;
            if (measured < time) {
                fail("Timer not waited expected time");
            }            
        }
        instance.stop();
        assertFalse(instance.hasNext());        
    }

    /**
     * Test of next method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testNext() {
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();
        assertFalse(instance.hasNext());
        
        long actionCount = 10;
        long time = 10;
        boolean setupTimer = instance.setupTimer(Integer.SIZE, actionCount, time);
        assertTrue(setupTimer);
        
        long before,after;
        before = System.currentTimeMillis();
        while(instance.hasNext()){
            instance.next();
            after = System.currentTimeMillis();
            if((after-before)<time){
                fail("Timer not waited expected time");
            }else{
                before = after;
            }
        }
    }

    /**
     * Test of getActionCount method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testGetActionCount() {
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();
        assertFalse(instance.hasNext());
        
        long actionCount = 10;
        long time = 10;
        boolean setupTimer = instance.setupTimer(Integer.SIZE, actionCount, time);
        assertTrue(setupTimer);
        
        long before,after;
        before = System.currentTimeMillis();
        long expected = 0;
        while(instance.hasNext()){
            assertEquals(expected++, instance.getActionCount());
            assertNotNull(instance.next());
            after = System.currentTimeMillis();
            if((after-before)<time){
                fail("Timer not waited expected time");
            }else{
                before = after;
            }
        }
        assertFalse(instance.hasNext());
        assertNull(instance.next());
    }

    /**
     * Test of setActionCount method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testSetActionCount() {
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();
        assertFalse(instance.hasNext());
        
        long actionCount = 10;
        long time = 10;
        boolean setupTimer = instance.setupTimer(Integer.SIZE, actionCount, time);
        assertTrue(setupTimer);

        long before,after;
        before = System.currentTimeMillis();
        long expected = 10;
        while(instance.hasNext()){
            assertNotNull(instance.next());
            after = System.currentTimeMillis();
            instance.setActionCount(--expected);
            if((after-before)<time){
                fail("Timer not waited expected time");
            }else{
                before = after;
            }
        }
        assertFalse(instance.hasNext());
        assertNull(instance.next());
        assertEquals(actionCount/2, instance.getActionCount());        
    }

    /**
     * Test of reset method, of class RunDelayIntervalImpl.
     */
//    @Test
//    public void testResetTimer() {
//        RunDelayIntervalImpl instance = new RunDelayIntervalImpl();
//        instance.setTime(1);
//        instance.setActionCount(-1);
//        
//        for (int i = 0; i < 2500; i++) {
//            double random = Math.random();
//            if (random > 0.5) {
//                instance.reset();
//                long actionCount = instance.getActionCount();
//                assertEquals(0, actionCount);
//            }
//        }
//        instance.reset();
//        long actionCount = instance.getActionCount();
//        assertEquals(0, actionCount);
//    }

    /**
     * Test of getTime method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testGetTime() {
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();
        long expResult = 1L;
        boolean setTime = instance.setTime(expResult);
        assertTrue(setTime);
        
        long result = instance.getTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTime method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testSetTime() {
        long time = 0L;
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();
        boolean result = instance.setTime(time);
        assertFalse(result);
        
        time = -1;
        result = instance.setTime(time);
        assertFalse(result);
        
        double sign, number;        
        for (int i = 0; i < 10000; i++) {
            sign = Math.random();
            number = Math.random();
            
            time = (long) (number * Long.MAX_VALUE);
            if (sign < 0.1) {
                time *= -1;
            }
            
            result = instance.setTime(time);
            if (time < 0) {
                assertFalse(result);
            } else {
                assertTrue(result);
            }
        }
    }

    /**
     * Test of stop method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testStop() {
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();

        long actionCount = -1;
        long time = 10;
        boolean setupTimer = instance.setupTimer(Integer.SIZE, actionCount, time);
        assertTrue(setupTimer);        
        
        long before,after;
        before = System.currentTimeMillis();
        while(instance.hasNext()){
            assertNotNull(instance.next());
            after = System.currentTimeMillis();
            if((after-before)<time){
                fail("Timer not waited expected time");
            }else{
                before = after;
            }
            
            double random = Math.random();
            if (random > 0.5) {
                instance.stop();
            }
            
        }
        assertFalse(instance.hasNext());
        assertNull(instance.next());
    }

    /**
     * Test of setupTimer method, of class RunDelayIntervalImpl.
     */
    @Test
    public void testSetupTimer() {
        RunDelayIntervalImpl<Integer> instance = new RunDelayIntervalImpl<>();
        boolean result = instance.setupTimer(null, 10, 0);
        assertFalse(result);
        
        result = instance.setupTimer(null, 10, -1);
        assertFalse(result);        
        
        result = instance.setupTimer(null, -1, 1);
        assertTrue(result);
    }
}
