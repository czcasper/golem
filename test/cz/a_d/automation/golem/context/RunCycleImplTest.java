package cz.a_d.automation.golem.context;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.common.iterators.ResetableIterator;
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
public class RunCycleImplTest {

    private AddressArrayList<Object> steps;
    private RunCycleImpl<Object> initialized;
    private ResetableIterator<Object> it;

    public RunCycleImplTest() {
        steps = new AddressArrayList<>();
        steps.add(new Integer(0));
        steps.add(new Integer(1));
        steps.add(new Integer(2));
        steps.add(new Integer(3));
        steps.add(new Integer(4));
        steps.add(new Integer(5));
        steps.add(new Integer(6));
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        it = new ResetableIterator<>(steps.iterator());
        initialized = new RunCycleImpl<>(steps, it);
        initialized.setupCycle(steps.get(0), 5, steps.size() - 1);
    }

    @After
    public void tearDown() {
        initialized = null;
    }

    /**
     * This method is testing hasNext implementation for RunCycleImpl.
     *
     * Test case one: Initialized cycle by number of object iterated from start
     * to end by multiple times specified in setup method.
     *
     * Test case two: This test case iterate from start to half of array and
     * after that use cycle command continue for skiping rest of cycle
     *
     * Test case three: This test case testing break functionality in connection
     * with hasNext function. Whole cycle is ended in middle.
     *
     */
    @Test
    public void testHasNext() {
        assertTrue(initialized.hasNext());
        for (int i = 0; i < initialized.getRepeatCount(); i++) {
            Iterator<Object> tmpIt = steps.iterator();
            for (int j = 0; j < steps.size(); j++) {
                assertTrue(initialized.hasNext());
                Object result = initialized.next();
                assertNotNull(result);
                assertSame(tmpIt.next(), result);
            }
        }
        assertFalse(initialized.hasNext());
        assertFalse(it.hasNext());
        assertEquals(initialized.getCycleIterationNum(), initialized.getRepeatCount());

        initialized.reset();
        it.setIt(steps.iterator());
        assertTrue(initialized.hasNext());
        for (int i = 0; i < initialized.getRepeatCount(); i++) {
            Iterator<Object> tmpIt = steps.iterator();
            for (int j = 0; j < (steps.size() / 2); j++) {
                assertTrue(initialized.hasNext());
                Object result = initialized.next();
                assertNotNull(result);
                assertSame(tmpIt.next(), result);
            }
            initialized.setContinue();
        }
        assertFalse(initialized.hasNext());
        assertFalse(it.hasNext());
        assertEquals(initialized.getCycleIterationNum(), initialized.getRepeatCount());


        initialized.reset();
        it.setIt(steps.iterator());
        assertTrue(initialized.hasNext());

        testLoop:
        for (int i = 0; i < initialized.getRepeatCount(); i++) {
            Iterator<Object> tmpIt = steps.iterator();
            for (int j = 0; j < steps.size(); j++) {
                assertTrue(initialized.hasNext());
                Object result = initialized.next();
                assertNotNull(result);
                assertSame(tmpIt.next(), result);
                if ((i > (initialized.getRepeatCount() / 2)) && (j > (steps.size() / 2))) {
                    initialized.setBreak();
                    break testLoop;
                }
            }
        }
        assertFalse(initialized.hasNext());
        assertFalse(it.hasNext());
    }

    /**
     * Test of setupCycle method, of class RunCycleImpl.
     */
    @Test
    public void testSetupCycle() {
        Object rootAction = null;
        int repeatCount = 0;
        int actionCount = -1;
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps, it);

        boolean result = instance.setupCycle(rootAction, repeatCount, actionCount);
        assertFalse(result);

        rootAction = steps.get(0);
        result = instance.setupCycle(rootAction, repeatCount, actionCount);
        assertFalse(result);

        actionCount = steps.size() - 1;
        result = instance.setupCycle(rootAction, repeatCount, actionCount);
        assertFalse(result);

        repeatCount = -1;
        result = instance.setupCycle(rootAction, repeatCount, actionCount);
        assertTrue(result);

        repeatCount = 1;
        result = instance.setupCycle(rootAction, repeatCount, actionCount);
        assertTrue(result);

        actionCount = 0;
        result = instance.setupCycle(rootAction, repeatCount, actionCount);
        assertTrue(result);
    }

    /**
     * Test of next method, of class RunCycleImpl.
     */
    @Test
    public void testNext() {
        //TODO refactor test logic 
        Iterator<Object> current = null;
        Iterator<Object> valid = null;
        while (initialized.hasNext()) {
            if (initialized.isFirstAction()) {
                assertNotSame(current, it.getIt());
                current = it.getIt();
                valid = steps.iterator();
            } else {
                assertSame(current, it.getIt());
            }
            Object result = initialized.next();
            assertSame(valid.next(), result);
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertEquals(steps.size(), initialized.getActionIndex());
        assertNull(initialized.next());

        initialized.reset();
        it.setIt(steps.iterator());
        assertTrue(initialized.hasNext());

        boolean setupCycle = initialized.setupCycle(steps.get(0), 5, steps.size() / 2);
        assertTrue(setupCycle);

        while (initialized.hasNext()) {
            if (initialized.isFirstAction()) {
                assertNotSame(current, it.getIt());
                current = it.getIt();
                valid = steps.iterator();
            } else {
                assertSame(current, it.getIt());
            }
            Object result = initialized.next();
            assertSame(valid.next(), result);
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertEquals((steps.size() / 2) + 1, initialized.getActionIndex());
        assertNull(initialized.next());

        initialized.reset();
        it.setIt(steps.iterator());
        assertTrue(initialized.hasNext());

        int halfSize = steps.size() / 2;
        setupCycle = initialized.setupCycle(steps.get(halfSize), 5, halfSize);
        assertTrue(setupCycle);
        for (int i = 0; i < halfSize; i++) {
            it.next();
        }

        while (initialized.hasNext()) {
            if (initialized.isFirstAction()) {
                assertNotSame(current, it.getIt());
                current = it.getIt();
                valid = steps.listIterator(halfSize);
            } else {
                assertSame(current, it.getIt());
            }
            Object result = initialized.next();
            assertSame(valid.next(), result);
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertEquals((steps.size() / 2) + 1, initialized.getActionIndex());
        assertNull(initialized.next());
    }

    /**
     * Test of getStartAction method, of class RunCycleImpl.
     */
    @Test
    public void testGetStartAction() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        Object result = instance.getStartAction();
        assertNull(result);
        Object expResult;

        for (int index = 0; index < steps.size(); index++) {
            expResult = steps.get(index);
            boolean test = initialized.setupCycle(expResult, 10, 0);
            assertTrue(test);
            result = initialized.getStartAction();
            assertSame(expResult, result);
        }
    }

    /**
     * Test of shiftStartAction method, of class RunCycleImpl.
     */
    @Test
    public void testShiftRootAction() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);

        boolean result = instance.shiftStartAction(10);
        assertFalse(result);

        result = initialized.shiftStartAction(-1);
        assertFalse(result);

        result = initialized.shiftStartAction(0);
        assertFalse(result);

        result = initialized.shiftStartAction(steps.size());
        assertFalse(result);

        result = initialized.shiftStartAction(steps.size() - 1);
        assertTrue(result);

        int actionCount = 2;
        boolean setupCycle = initialized.setupCycle(steps.get(0), steps.size(), actionCount);
        assertTrue(setupCycle);

        result = initialized.shiftStartAction(actionCount + 1);
        assertFalse(result);

        result = initialized.shiftStartAction(actionCount);
        assertTrue(result);

        result = initialized.shiftStartAction(-(actionCount + 1));
        assertFalse(result);

        result = initialized.shiftStartAction(-actionCount);
        assertTrue(result);

        actionCount = steps.size() - 1;
        setupCycle = initialized.setupCycle(steps.get(0), steps.size(), actionCount);
        assertTrue(setupCycle);

        Iterator<Object> valid = steps.iterator();
        int index = 0;
        Object tmp;
        while (initialized.hasNext()) {
            Object next = initialized.next();
            if (initialized.isFirstAction()) {
                initialized.shiftStartAction(1);
            }
            tmp = valid.next();
            if (initialized.isFirstAction()) {
                if (initialized.startAction == initialized.endAction) {
                    index++;
                }

                valid = steps.listIterator(index++);
            }
            assertSame(tmp, next);
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertEquals(1, initialized.getActionIndex());
        assertNull(initialized.next());
    }

    /**
     * Test of getEndAction method, of class RunCycleImpl.
     */
    @Test
    public void testGetEndAction() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        Object result = instance.getEndAction();
        assertNull(result);
        Object expResult;
        for (int index = 0; index < steps.size(); index++) {
            expResult = steps.get(index);
            boolean test = initialized.setupCycle(expResult, 10, 0);
            assertTrue(test);
            result = initialized.getEndAction();
            assertSame(expResult, result);
        }
        for (int index = 0; index < steps.size(); index++) {
            expResult = steps.get(index);
            boolean test = initialized.setupCycle(steps.get(0), 10, index);
            assertTrue(test);
            result = initialized.getEndAction();
            assertSame(expResult, result);
        }
    }

    /**
     * Test of shiftEndAction method, of class RunCycleImpl.
     */
    @Test
    public void testShiftEndAction() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        boolean result = instance.shiftEndAction(10);
        assertFalse(result);

        result = initialized.shiftEndAction(1);
        assertFalse(result);

        result = initialized.shiftEndAction(0);
        assertFalse(result);

        int actionCount = steps.size();
        result = initialized.shiftEndAction(-actionCount);
        assertFalse(result);

        actionCount -= 1;
        result = initialized.shiftEndAction(-actionCount);
        assertTrue(result);

        result = initialized.shiftEndAction(-1);
        assertFalse(result);

        actionCount = steps.size() - 1;
        boolean setupCycle = initialized.setupCycle(steps.get(0), steps.size(), actionCount);
        assertTrue(setupCycle);

        Iterator<Object> valid = steps.iterator();
        while (initialized.hasNext()) {
            Object next = initialized.next();
            Object tmp = valid.next();
            if (initialized.isFirstAction()) {
                initialized.shiftEndAction(-1);
                valid = steps.iterator();
            }
            assertSame(tmp, next);
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertEquals(1, initialized.getActionIndex());
        assertNull(initialized.next());
    }

    /**
     * Test of isContinue method, of class RunCycleImpl.
     */
    @Test
    public void testSetContinue() {
        boolean setupCycle = initialized.setupCycle(steps.get(0), steps.size(), steps.size() - 1);
        assertTrue(setupCycle);

        Iterator<Object> valid = null;
        int index = 0, range = steps.size();
        while (initialized.hasNext()) {
            if (initialized.isFirstAction()) {
                valid = steps.iterator();
                range--;
            }
            Object result = initialized.next();
            assertSame(valid.next(), result);
            if ((index++) == range) {
                initialized.setContinue();
                index = 0;
            }
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertEquals(2, initialized.getActionIndex());
        assertFalse(it.hasNext());
    }

    /**
     * Test of setBreak method, of class RunCycleImpl.
     */
    @Test
    public void testSetBreak() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        boolean result = instance.isBreak();
        assertFalse(result);

        instance.setBreak();
        assertTrue(instance.isBreak());

        initialized.reset();
        it.setIt(steps.iterator());
        assertTrue(initialized.hasNext());

        testLoop:
        for (int i = 0; i < initialized.getRepeatCount(); i++) {
            Iterator<Object> tmpIt = steps.iterator();
            for (int j = 0; j < steps.size(); j++) {
                assertTrue(initialized.hasNext());
                Object next = initialized.next();
                assertNotNull(next);
                assertSame(tmpIt.next(), next);
                if ((i > (initialized.getRepeatCount() / 2)) && (j > (steps.size() / 2))) {
                    initialized.setBreak();
                    break testLoop;
                }
            }
        }
        assertFalse(initialized.hasNext());
        assertTrue((initialized.getCycleIterationNum() > initialized.getRepeatCount() / 2));
        assertFalse(it.hasNext());
    }

    /**
     * Test of getCycleIterationNum method, of class RunCycleImpl.
     */
    @Test
    public void testGetCycleIterationNum() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        long result = instance.getCycleIterationNum();
        assertEquals(0l, result);

        Iterator<Object> valid = null;
        int index = 0;
        while (initialized.hasNext()) {
            if (initialized.isFirstAction()) {
                assertEquals(index++, initialized.getCycleIterationNum());
                valid = steps.iterator();
            }
            Object next = initialized.next();
            assertSame(valid.next(), next);
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertFalse(it.hasNext());
    }

    /**
     * Test of getActionIndex method, of class RunCycleImpl.
     */
    @Test
    public void testGetActionIndex() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        int result = instance.getActionIndex();
        assertEquals(0, result);

        Iterator<Object> valid = null;
        int index = 0;
        while (initialized.hasNext()) {
            if (initialized.isFirstAction()) {
                valid = steps.iterator();
                index = 0;
            }
            assertEquals(index++, initialized.getActionIndex());
            Object next = initialized.next();
            assertSame(valid.next(), next);
        }
        assertEquals(initialized.getRepeatCount(), initialized.getCycleIterationNum());
        assertFalse(it.hasNext());
    }

    /**
     * Test of getRepeatCount method, of class RunCycleImpl.
     */
    @Test
    public void testGetRepeatCount() {
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        long result = instance.getRepeatCount();
        assertEquals(0L, result);

        result = initialized.getRepeatCount();
        assertEquals(5, result);

        long repeatCount = 20;
        initialized.setupCycle(steps.get(0), repeatCount, steps.size() - 1);
        assertEquals(repeatCount, initialized.getRepeatCount());
    }

    /**
     * Test of setRepeatCount method, of class RunCycleImpl.
     */
    @Test
    public void testSetRepeatCount() {
        int repeatCount = 0;
        RunCycleImpl<Object> instance = new RunCycleImpl<>(steps);
        instance.setRepeatCount(repeatCount);
        assertEquals(repeatCount, instance.getRepeatCount());

        repeatCount = 20;
        initialized.setupCycle(steps.get(0), repeatCount, steps.size() - 1);
        assertEquals(repeatCount, initialized.getRepeatCount());

        Iterator<Object> valid = null;
        int index = steps.size() - 1;
        while (initialized.hasNext()) {
            if (initialized.isFirstAction()) {
                valid = steps.iterator();
            }
            Object next = initialized.next();
            assertSame(valid.next(), next);
            initialized.setRepeatCount(index--);
        }
        assertEquals(1, initialized.getCycleIterationNum());
        assertEquals(0, initialized.getRepeatCount());
        assertFalse(it.hasNext());
    }
}
