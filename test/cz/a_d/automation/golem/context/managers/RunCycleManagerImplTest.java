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
import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.context.RunCycleImpl;
import cz.a_d.automation.golem.interfaces.context.RunCycle;
import cz.a_d.automation.golem.interfaces.context.managers.RunCycleManager;
import cz.a_d.automation.golem.spools.actions.SimpleActionStream;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionForTestingContext;
import java.util.Iterator;
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
// TODO Documentation: Create JavaDoc on class, method and field level
public class RunCycleManagerImplTest {

    private AddressArrayList<ActionForTestingContext<Integer>> steps;
    private RunContextImpl<ActionForTestingContext<Integer>,Boolean, Object> initializedRun;

    public RunCycleManagerImplTest() {
        steps = new AddressArrayList<>();
        for (int i = 0; i < 10; i++) {
            steps.add(new ActionForTestingContext<>(Integer.valueOf(i)));
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
        initializedRun = new RunContextImpl<>();
        initializedRun.setActionStream(new SimpleActionStream<>(steps));
        initializedRun.setCycleManager(new RunCycleManagerImpl<>(initializedRun));
    }

    @After
    public void tearDown() {
        initializedRun = null;
    }

    /**
     * Test of updateCycleStep method, of class RunCycleManagerImpl.
     */
    @Test
    public void testHasNext() {
        /**
         * Initialize and test protection of method agains instance initialized by null RunContext.
         */
        RunCycleManager<ActionForTestingContext<Integer>, Object> instance = new RunCycleManagerImpl<>(null);
        assertFalse(instance.hasNext());

        /**
         * Testing instance created with valid RunContext and seting up first cycle.
         */
        instance = new RunCycleManagerImpl<>(initializedRun);
        assertFalse(instance.hasNext());
        long repeatCount = 2;
        boolean setup = instance.setup(steps.get(0), repeatCount, steps.size() - 1);
        assertTrue(setup);
        assertFalse(instance.hasNext());
        ResetableIterator<ActionForTestingContext<Integer>> it = initializedRun.resetableIterator();
        assertTrue(it.hasNext());

        /**
         * Testing feature interaction with break cycle feature.
         */
        instance = initializedRun.getCycleManager();
        setup = instance.setup(steps.get(0), repeatCount, steps.size() - 1);
        assertTrue(setup);
        initializedRun.next();
        assertTrue(instance.hasNext());

        setup = instance.setup(steps.get(0), repeatCount, steps.size() - 1);
        assertTrue(setup);

        instance.getCurrent().setBreak();
        assertTrue(instance.hasNext());

        instance.getCurrent().setBreak();
        assertFalse(instance.hasNext());

        /**
         * Test reusability of defined cycles after two succesfull break's.
         */
        it.setIt(steps.iterator());
        boolean firstRun = true;
        for (int i = 0; i < (2 * repeatCount); i++) {
            Iterator<ActionForTestingContext<Integer>> valid = steps.iterator();
            for (int j = 0; j < steps.size(); j++) {
                if (firstRun) {
                    firstRun = false;
                    assertFalse(instance.hasNext());
                } else {
                    assertTrue(instance.hasNext());
                }
                Object next = initializedRun.next();
                assertSame(valid.next(), next);
            }
        }
        assertFalse(instance.hasNext());
        assertFalse(initializedRun.hasNext());

        /**
         * 
         */
        it.setIt(steps.iterator());
        int halfSize = steps.size() / 2;
        setup = instance.setup(steps.get(halfSize), repeatCount, steps.size() - halfSize - 1);
        assertTrue(setup);
        setup = instance.setup(steps.get(halfSize), repeatCount, steps.size() - halfSize - 1);
        assertTrue(setup);
        firstRun = true;
        for (int i = 0; i < (2 * repeatCount); i++) {
            Iterator<ActionForTestingContext<Integer>> valid = steps.iterator();
            for (int j = 0; j < halfSize; j++) {
                if (firstRun) {
                    firstRun = false;
                    assertFalse(instance.hasNext());
                } else {
                    assertTrue(instance.hasNext());
                }
                Object next = initializedRun.next();
                assertSame(valid.next(), next);
            }
            for (int j = 0; j < (2 * repeatCount); j++) {
                valid = steps.listIterator(halfSize);
                for (int k = 0; k < halfSize; k++) {
                    assertTrue(instance.hasNext());
                    Object next = initializedRun.next();
                    assertSame(valid.next(), next);
                }
            }
        }
        assertFalse(instance.hasNext());
        assertFalse(initializedRun.hasNext());

        /**
         * 
         */
        it.setIt(steps.iterator());
        setup = instance.setup(steps.get(halfSize + 1), repeatCount, 0);
        assertTrue(setup);
        setup = instance.setup(steps.get(halfSize + 1), repeatCount, 0);
        assertTrue(setup);
        firstRun = true;
        for (int i = 0; i < (2 * repeatCount); i++) {
            Iterator<ActionForTestingContext<Integer>> valid = steps.iterator();
            for (int j = 0; j < halfSize; j++) {
                if (firstRun) {
                    firstRun = false;
                    assertFalse(instance.hasNext());
                } else {
                    assertTrue(instance.hasNext());
                }
                Object next = initializedRun.next();
                assertSame(valid.next(), next);
            }
            for (int j = 0; j < (2 * repeatCount); j++) {
                valid = steps.listIterator(halfSize);
                Object val = null;
                for (int k = 0; k < halfSize; k++) {
                    if (k == 2) {
                        for (int l = 0; l < (2 * repeatCount); l++) {
                            assertTrue(instance.hasNext());
                            Object next = initializedRun.next();
                            assertSame(val, next);
                        }
                        assertTrue(instance.hasNext());
                        Object next = initializedRun.next();
                        assertSame(valid.next(), next);
                        val = null;
                    } else {
                        assertTrue(instance.hasNext());
                        Object next = initializedRun.next();
                        if (k == 1) {
                            val = valid.next();
                            assertSame(val, next);
                        } else {
                            assertSame(valid.next(), next);
                        }
                    }
                }
            }
        }
        assertFalse(instance.hasNext());
        assertFalse(initializedRun.hasNext());
    }

    /**
     * Test of next method, of class RunCycleManagerImpl.
     */
    @Test
    public void testNext() {

        long repeatCount = 2;
        RunCycleManager<ActionForTestingContext<Integer>, Object> instance = initializedRun.getCycleManager();
        ResetableIterator<ActionForTestingContext<Integer>> it = initializedRun.resetableIterator();

        boolean setup = instance.setup(steps.get(0), repeatCount, steps.size() - 1);
        assertTrue(setup);

        Iterator<ActionForTestingContext<Integer>> validIt = steps.iterator();
        boolean first = true;
        while (initializedRun.hasNext()) {
            assertSame(validIt.next(), initializedRun.next());
            RunCycle tmp = instance.getCurrent();
            if (first) {
                first = false;
            } else if (tmp.isFirstAction()) {
                validIt = steps.iterator();
            }
        }
        assertFalse(instance.hasNext());
        assertFalse(initializedRun.hasNext());

        it.setIt(steps.iterator());
        setup = instance.setup(steps.get(0), repeatCount, 0);
        assertTrue(setup);

        validIt = steps.iterator();
        first = true;
        RunCycle cycle = instance.getCurrent();
        RunCycleImpl tmp;
        while (initializedRun.hasNext()) {
            assertSame(validIt.next(), initializedRun.next());
            if (first) {
                cycle = instance.getCurrent();
            }

            tmp = (RunCycleImpl) instance.getCurrent();
            if (tmp == cycle) {

                if (first) {
                    first = false;
                } else if (tmp.isFirstAction()) {
                    validIt = steps.iterator();
                } else if (!tmp.hasNext()) {
                    validIt = steps.iterator();
                }
            } else if ((tmp != null) && (tmp.hasNext())) {
                validIt = steps.iterator();
            }

        }
        assertFalse(instance.hasNext());
        assertFalse(initializedRun.hasNext());
    }

    /**
     * Test of setup method, of class RunCycleManagerImpl.
     */
    @Test
    public void testInitCycle() {
        ActionForTestingContext<Integer> action = null;
        long repeatCount = 0L;
        int actionCount = 0;
        RunCycleManager<ActionForTestingContext<Integer>, Object> instance = new RunCycleManagerImpl<>(null);
        boolean result = instance.setup(action, repeatCount, actionCount);
        assertFalse(result);

        instance = initializedRun.getCycleManager();
        result = instance.setup(action, repeatCount, actionCount);
        assertFalse(result);

        repeatCount = 2;
        for (ActionForTestingContext<Integer> step : steps) {
            result = instance.setup(step, repeatCount, 0);
            assertTrue(result);
        }

        Iterator<ActionForTestingContext<Integer>> validIt = steps.iterator();
        int counter = 0;
        Object tmp = validIt.next();
        for (Object o : initializedRun) {
            if (counter == (repeatCount + 1)) {
                tmp = validIt.next();
                counter = 0;
            }
            assertSame(tmp, o);
            counter++;
        }
        assertFalse(instance.hasNext());
        assertFalse(initializedRun.hasNext());


//        assertFalse(instance.isInCycle());
//        assertSame(action, initializedRun.next());
//        assertTrue(instance.isInCycle());
//        assertSame(action, initializedRun.next());
//        assertTrue(instance.isInCycle());
    }

    /**
     * Test of getCurrent method, of class RunCycleManagerImpl.
     */
    @Test
    public void testGetCurrentCycle() {
        RunCycleManager<ActionForTestingContext<Integer>, Object> instance = new RunCycleManagerImpl<>(null);
        RunCycle result = instance.getCurrent();
        assertNull(result);

        instance = initializedRun.getCycleManager();
        assertTrue(instance.setup(steps.get(0), 4, steps.size() - 1));
        assertTrue(instance.setup(steps.get(7), 3, steps.size() - 8));

        result = instance.getCurrent();
        assertNull(result);

        boolean breakFlag = false;
        int counterI = 0;
        int counterJ = 0;

        for (Object i : initializedRun) {
            result = instance.getCurrent();
            if (counterI < 7) {
                assertEquals(steps.get(0), result.getStartAction());
                counterI++;
                counterJ = 0;
            } else {
                assertEquals(steps.get(7), result.getStartAction());
                if (counterJ < 8) {
                    counterJ++;
                    if (breakFlag) {
                        result.setBreak();
                        counterI = 0;
                    }
                } else {
                    counterI = 0;
                    breakFlag = true;
                }

            }
        }
    }
}
