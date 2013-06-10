/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.managers;

import com.ca.automation.golem.common.AddressArrayList;
import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.spools.actions.SimpleActionStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author maslu02
 */
public class RunActionStackManagerTest {

    private AddressArrayList<Object> steps;
    private RunContextImpl initializedRun;

    public RunActionStackManagerTest() {
        steps = new AddressArrayList<Object>();
        for (int i = 0; i < 5; i++) {
            steps.add(new Integer(i));
        }
    }

    @Before
    public void setUp() {
        initializedRun = new RunContextImpl();
        initializedRun.setActionStream(new SimpleActionStream(steps));
        initializedRun.setStackManager(new RunActionStackManagerImpl(initializedRun));
    }

    @After
    public void tearDown() {
        initializedRun = null;
    }

    @Test
    public void testHasNext() throws Exception {
        RunActionStackManagerImpl<Object,Boolean,Object> instance = new RunActionStackManagerImpl<Object,Boolean,Object>(null);
        assertFalse(instance.hasNext());

        instance = (RunActionStackManagerImpl<Object,Boolean,Object>) initializedRun.getStackManager();
        List<Object> actions = new ArrayList<Object>();
        for (int i = 0; i < 7; i++) {
            actions.add(i);
        }
        Object[] tmp = new Object[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setup(steps.get(0), tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);

        boolean result = instance.hasNext();
        assertFalse(result);

        initializedRun.next();
        Iterator<Object> it = actions.iterator();
        for (int i = 0; i < actions.size(); i++) {
            result = instance.hasNext();
            assertTrue(result);
            Object next = initializedRun.next();
            assertSame(it.next(), next);
        }
        result = instance.hasNext();
        assertFalse(result);

        setupStack = instance.setup(steps.get(0), tmp);
        assertTrue(setupStack);
        initializedRun.resetableIterator().setIt(steps.iterator());
        result = instance.hasNext();
        assertFalse(result);

        initializedRun.next();

        for (int i = 0; i < 2 * actions.size(); i++) {
            if (!it.hasNext()) {
                it = actions.iterator();
            }
            result = instance.hasNext();
            assertTrue(result);
            Object next = initializedRun.next();
            assertSame(it.next(), next);
        }
        result = instance.hasNext();
        assertFalse(result);
    }

    @Test
    public void testNext() {
        RunActionStackManagerImpl<Object,Boolean,Object> instance = new RunActionStackManagerImpl<Object,Boolean,Object>(null);
        assertNull(instance.next());

        instance = (RunActionStackManagerImpl<Object,Boolean,Object>) initializedRun.getStackManager();
        List<Object> actions = new ArrayList<Object>();
        for (int i = 0; i < 7; i++) {
            actions.add(i);
        }
        Object[] tmp = new Object[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setup(steps.get(0), tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);

        boolean first = true, hasNext = false;
        Iterator<Object> stackIt = actions.iterator();
        Iterator<Object> dataIt = steps.iterator();
        for (Object o : initializedRun) {

            if (first) {
                assertSame(dataIt.next(), o);
                first = false;
            } else {
                if (hasNext) {
                    assertSame(stackIt.next(), o);
                } else {
                    assertSame(dataIt.next(), o);
                }
            }
            hasNext = instance.hasNext();
        }
    }

    @Test
    public void testSetup() {
        RunActionStackManagerImpl<Object,Boolean,Object> instance = (RunActionStackManagerImpl<Object,Boolean,Object>) initializedRun.getStackManager();

        List<Object> actions = new ArrayList<Object>();
        for (int i = 0; i < 7; i++) {
            actions.add(i);
        }
        Object[] tmp = new Object[actions.size()];
        tmp = actions.toArray(tmp);
        for (Object s : steps) {
            boolean setupStack = instance.setup(s, tmp);
            assertTrue(setupStack);
        }
        Collections.reverse(actions);

        boolean first = true, hasNext = false;
        Iterator<Object> stackIt = actions.iterator();
        Iterator<Object> dataIt = steps.iterator();
        for (Object o : initializedRun) {
            if (first) {
                assertSame(dataIt.next(), o);
                first = false;
            } else {
                if (hasNext) {
                    if(!stackIt.hasNext()){
                        stackIt = actions.iterator();
                    }
                    assertSame(stackIt.next(), o);
                } else {
                    assertSame(dataIt.next(), o);
                }
            }
            hasNext = instance.hasNext();
        }
    }
}
