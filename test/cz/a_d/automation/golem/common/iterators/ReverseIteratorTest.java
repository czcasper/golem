/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.common.iterators;

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
 * @author casper
 */
public class ReverseIteratorTest {

    private List<Object> steps;
    @Rule
    public ExpectedException testException = ExpectedException.none();

    public ReverseIteratorTest() {
        steps = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            steps.add(i);
        }
    }

    @Test
    public void testConstructor() {
        testException.expect(NullPointerException.class);
        ReverseIterator<Object> instance = new ReverseIterator<>(null);
    }

    /**
     * Test of hasNext method, of class ReverseIterator.
     */
    @Test
    public void testHasNext() {
        ReverseIterator<Object> instance = new ReverseIterator<>(steps);
        boolean result = instance.hasNext();
        assertTrue(result);

        int index = steps.size() - 1;
        for (Object o : instance) {
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
        ReverseIterator<Object> instance = new ReverseIterator<>(steps);
        boolean result = instance.hasNext();
        assertTrue(result);
        
        int index = steps.size() - 1;
        while (instance.hasNext()) {
            Object next = instance.next();
            assertSame(steps.get(index--), next);
        }        
    }

    /**
     * Test of remove method, of class ReverseIterator.
     */
    @Test
    public void testRemove() {
        List<Object> testData = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            testData.add(i);
        }

        ReverseIterator<Object> instance = new ReverseIterator<>(testData);

        double rand;
        int index = testData.size() - 1;
        while (instance.hasNext()) {
            assertSame(testData.get(index--), instance.next());
            instance.remove();
        }
        assertEquals(-1, index);
    }

    /**
     * Test of iterator method, of class ReverseIterator.
     */
    @Test
    public void testIterator() {
        ReverseIterator<Object> instance = new ReverseIterator<>(steps);
        int index = steps.size() - 1;
        for (Object o : instance) {
            assertSame(steps.get(index--), o);
        }
        assertEquals(-1, index);
    }
}
