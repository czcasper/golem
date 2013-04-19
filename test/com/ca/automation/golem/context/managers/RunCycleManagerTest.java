/*
 */
package com.ca.automation.golem.context.managers;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.context.RunCycle;
import com.ca.automation.golem.context.actionInterfaces.RunCycleContext;
import java.util.Iterator;
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
public class RunCycleManagerTest {

    private AddressArrayList<Object> steps;
    private RunContextImpl initializedRun;

    public RunCycleManagerTest() {
        steps = new AddressArrayList<Object>();
        for (int i = 0; i < 10; i++) {
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
        initializedRun.setCycleManager(new RunCycleManager(initializedRun));
    }

    @After
    public void tearDown() {
        initializedRun = null;
    }

    /**
     * Test of updateCycleStep method, of class RunCycleManager.
     */
    @Test
    public void testHasNext() {
        RunCycleManager instance = new RunCycleManager(null);
        assertFalse(instance.hasNext());

        instance = new RunCycleManager(initializedRun);
        assertFalse(instance.hasNext());
        long repeatCount = 2;
        boolean setup = instance.setup(steps.get(0), repeatCount, steps.size() - 1);
        assertTrue(setup);
        assertFalse(instance.hasNext());
        ResetableIterator it = initializedRun.getIt();
        assertTrue(it.hasNext());

        instance = (RunCycleManager) initializedRun.getCycleManager();
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

        it.setIt(steps.iterator());
        boolean firstRun = true;
        for (int i = 0; i < (2 * repeatCount); i++) {
            Iterator<Object> valid = steps.iterator();
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

        it.setIt(steps.iterator());
        int halfSize = steps.size() / 2;
        setup = instance.setup(steps.get(halfSize), repeatCount, steps.size() - halfSize - 1);
        assertTrue(setup);
        setup = instance.setup(steps.get(halfSize), repeatCount, steps.size() - halfSize - 1);
        assertTrue(setup);

        firstRun = true;
        for (int i = 0; i < (2 * repeatCount); i++) {
            Iterator<Object> valid = steps.iterator();
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

        it.setIt(steps.iterator());
        setup = instance.setup(steps.get(halfSize + 1), repeatCount, 0);
        assertTrue(setup);
        setup = instance.setup(steps.get(halfSize + 1), repeatCount, 0);
        assertTrue(setup);

        firstRun = true;
        for (int i = 0; i < (2 * repeatCount); i++) {
            Iterator<Object> valid = steps.iterator();
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
     * Test of next method, of class RunCycleManager.
     */
    @Test
    public void testNext() {

        long repeatCount = 2;
        RunCycleManager instance = (RunCycleManager) initializedRun.getCycleManager();
        ResetableIterator it = initializedRun.getIt();

        boolean setup = instance.setup(steps.get(0), repeatCount, steps.size() - 1);
        assertTrue(setup);

        Iterator<Object> validIt = steps.iterator();
        boolean first = true;
        while (initializedRun.hasNext()) {
            assertSame(validIt.next(), initializedRun.next());
            RunCycleContext tmp = instance.getCurrent();
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
        RunCycleContext cycle = instance.getCurrent();
        RunCycle tmp;
        while (initializedRun.hasNext()) {
            assertSame(validIt.next(), initializedRun.next());
            if (first) {
                cycle = instance.getCurrent();
            }

            tmp = (RunCycle) instance.getCurrent();
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
     * Test of setup method, of class RunCycleManager.
     */
    @Test
    public void testInitCycle() {
        Object action = null;
        long repeatCount = 0L;
        int actionCount = 0;
        RunCycleManager instance = new RunCycleManager(null);
        boolean result = instance.setup(action, repeatCount, actionCount);
        assertFalse(result);

        instance = (RunCycleManager) initializedRun.getCycleManager();
        result = instance.setup(action, repeatCount, actionCount);
        assertFalse(result);

        repeatCount = 2;
        for (Object step : steps) {
            result = instance.setup(step, repeatCount, 0);
            assertTrue(result);
        }

        Iterator<Object> validIt = steps.iterator();
        int counter = 0;
        Object tmp = validIt.next();
        for (Object o : initializedRun) {
            if (counter == (repeatCount+1)) {
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
     * Test of getCurrent method, of class RunCycleManager.
     */
    @Test
    public void testGetCurrentCycle() {
        RunCycleManager instance = new RunCycleManager(null);
        RunCycleContext result = instance.getCurrent();
        assertNull(result);

        instance = (RunCycleManager) initializedRun.getCycleManager();
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
