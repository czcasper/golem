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
import cz.a_d.automation.golem.interfaces.context.managers.RunActionStackManager;
import cz.a_d.automation.golem.spools.actions.SimpleActionStream;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionForTestingContext;
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
 * @author casper
 */
// TODO Documentation: Create JavaDoc on class and method level
// TODO Refactoring: Improve test to fully cover functionality expeceted from this class.
public class RunActionStackManagerTest {

    private AddressArrayList<ActionForTestingContext<Integer>> steps;
    private RunContextImpl<ActionForTestingContext<Integer>, Boolean, Object> initializedRun;

    public RunActionStackManagerTest() {
        steps = new AddressArrayList<>();
        for (int i = 0; i < 5; i++) {
            steps.add(new ActionForTestingContext<>(Integer.valueOf(i)));
        }
    }

    @Before
    public void setup() {
        initializedRun = new RunContextImpl<>();
        initializedRun.setActionStream(new SimpleActionStream<>(steps));
        initializedRun.setStackManager(new RunActionStackManagerImpl<>(initializedRun));
    }

    @After
    public void clean() {
        initializedRun = null;
    }

    @Test
    public void testHasNext() throws Exception {
        RunActionStackManager<ActionForTestingContext<Integer>, Object> instance = new RunActionStackManagerImpl<>(null);
        assertFalse(instance.hasNext());

        instance = initializedRun.getStackManager();
        List<Object> actions = new ArrayList<>();
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
        RunActionStackManager<ActionForTestingContext<Integer>, Object> instance = new RunActionStackManagerImpl<>(null);
        assertNull(instance.next());

        instance = initializedRun.getStackManager();
        List<ActionForTestingContext<Integer>> actions = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            actions.add(new ActionForTestingContext<>(i));
        }
        Object[] tmp = new Object[actions.size()];
        tmp = actions.toArray(tmp);
        boolean setupStack = instance.setup(steps.get(0), tmp);
        assertTrue(setupStack);
        Collections.reverse(actions);

        boolean first = true, hasNext = false;
        Iterator<ActionForTestingContext<Integer>> stackIt = actions.iterator();
        Iterator<ActionForTestingContext<Integer>> dataIt = steps.iterator();
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
        RunActionStackManager<ActionForTestingContext<Integer>, Object> instance = initializedRun.getStackManager();

        List<ActionForTestingContext<Integer>> actions = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            actions.add(new ActionForTestingContext<>(i));
        }
        Object[] tmp = new Object[actions.size()];
        tmp = actions.toArray(tmp);
        for (ActionForTestingContext<Integer> s : steps) {
            boolean setupStack = instance.setup(s, tmp);
            assertTrue(setupStack);
        }
        Collections.reverse(actions);

        boolean first = true, hasNext = false;
        Iterator<ActionForTestingContext<Integer>> stackIt = actions.iterator();
        Iterator<ActionForTestingContext<Integer>> dataIt = steps.iterator();
        for (Object o : initializedRun) {
            if (first) {
                assertSame(dataIt.next(), o);
                first = false;
            } else {
                if (hasNext) {
                    if (!stackIt.hasNext()) {
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
