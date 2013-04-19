/*
 */
package com.ca.automation.golem.context.managers;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.context.RunContextImpl;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author maslu02
 */
public class RunDelayIntervalManagerTest {

    private AddressArrayList<Object> steps;
    private RunContextImpl initializedRun;

    public RunDelayIntervalManagerTest() {
        steps = new AddressArrayList<Object>();
        for (int i = 0; i < 5; i++) {
            steps.add(new Integer(i));
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
        initializedRun = new RunContextImpl();
        initializedRun.setSteps(steps);
        initializedRun.setTimerManager(new RunDelayIntervalManager(initializedRun));
    }

    @After
    public void tearDown() {
        initializedRun = null;
    }

    /**
     * Test of hasNext method, of class RunDelayIntervalManager.
     */
    @Test
    public void testHasNext() throws Exception {
        RunDelayIntervalManager instance = new RunDelayIntervalManager(null);
        assertFalse(instance.hasNext());

        long actionCount = steps.size();
        long time = 10;
        instance = (RunDelayIntervalManager) initializedRun.getTimerManager();
        boolean setupTimer = instance.setup(steps.get(0), actionCount, time);
        assertTrue(setupTimer);

        long measured;
        boolean first = true;
        for (long i = 0; i < actionCount; i++) {
            if (first) {
                assertFalse(instance.hasNext());
                first = false;
            } else {
                assertTrue(instance.hasNext());
            }

            measured = System.currentTimeMillis();
            initializedRun.next();
            measured = System.currentTimeMillis() - measured;
            if (measured < time) {
                fail("Timer not waited expected time" + i);
            }
        }
        assertFalse(instance.hasNext());
        setupTimer = instance.setup(steps.get(1), actionCount - 1, time);
        assertTrue(setupTimer);
    }

    /**
     * Test of setup method, of class RunDelayIntervalManager.
     */
    @Test
    public void testNext() {
        RunDelayIntervalManager instance = new RunDelayIntervalManager(null);
        assertFalse(instance.hasNext());

        long actionCount = steps.size();
        long time = 10;
        instance = (RunDelayIntervalManager) initializedRun.getTimerManager();

        for (int i = 0; i < steps.size(); i++) {
            boolean setupTimer = instance.setup(steps.get(i), actionCount-i, time);
            assertTrue(setupTimer);
        }
        
        long measured,expect = time;
        boolean first = true;
        for (long i = 0; i < actionCount; i++) {
            if (first) {
                assertFalse(instance.hasNext());
                first = false;
            } else {
                assertTrue(instance.hasNext());
            }

            measured = System.currentTimeMillis();
            initializedRun.next();
            measured = System.currentTimeMillis() - measured;
            if (measured < expect) {
                fail("Timer not waited expected time" + i);
            }
            expect+=time;
        }
        assertFalse(instance.hasNext());
    }
    
    @Test
    public void testSetup(){
        RunDelayIntervalManager instance = new RunDelayIntervalManager(null);
        assertFalse(instance.hasNext());

        long actionCount = steps.size();
        long time = 10;
        instance = (RunDelayIntervalManager<Integer>) initializedRun.getTimerManager();
        
        for(int i=0;i<steps.size();i++){
             boolean setup = instance.setup(steps.get(0), i+2, time);
            assertTrue(setup);
        }
        
        long measured;
        boolean first = true;
        for (long i = 0; i < actionCount-1; i++) {
            if (first) {
                assertFalse(instance.hasNext());
                first = false;
            } else {
                   assertTrue(instance.hasNext());
            }

            measured = System.currentTimeMillis();
            initializedRun.next();
            measured = System.currentTimeMillis() - measured;
            if (measured < time) {
                fail("Timer not waited expected time" + i);
            }
            instance.getCurrent().stop();
        }
        assertFalse(instance.hasNext());
        
    }
}
