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
package cz.a_d.automation.golem.context.managers;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.interfaces.context.managers.RunDelayIntervalManager;
import cz.a_d.automation.golem.spools.actions.SimpleActionStream;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionForTestingContext;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author casper
 */
public class RunDelayIntervalManagerImplTest {

    private AddressArrayList<Object> steps;
    private RunContextImpl<Object, Boolean, Object> initializedRun;

    public RunDelayIntervalManagerImplTest() {
        steps = new AddressArrayList<>();
        for (int i = 0; i < 5; i++) {
            steps.add(new ActionForTestingContext<>(Integer.valueOf(i)));
        }

    }

    @Before
    public void setUp() {
        initializedRun = new RunContextImpl<>();
        initializedRun.setActionStream(new SimpleActionStream<>(steps));
        initializedRun.setDelayManager(new RunDelayIntervalManagerImpl<>(initializedRun));
    }

    @After
    public void tearDown() {
        initializedRun = null;
    }

    /**
     * Test of hasNext method, of class RunDelayIntervalManagerImpl.
     */
    @Test
    public void testHasNext() throws Exception {
        RunDelayIntervalManager<Object,Object> instance = new RunDelayIntervalManagerImpl<>(null);
        assertFalse(instance.hasNext());

        long actionCount = steps.size();
        long time = 10;
        instance = initializedRun.getDelayManager();
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
     * Test of setup method, of class RunDelayIntervalManagerImpl.
     */
    @Test
    public void testNext() {
        RunDelayIntervalManager<Object,Object> instance = new RunDelayIntervalManagerImpl<>(null);
        assertFalse(instance.hasNext());

        long actionCount = steps.size();
        long time = 10;
        instance = initializedRun.getDelayManager();

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
        RunDelayIntervalManager<Object,Object> instance = new RunDelayIntervalManagerImpl<>(null);
        assertFalse(instance.hasNext());

        long actionCount = steps.size();
        long time = 10;
        instance = initializedRun.getDelayManager();
        
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
