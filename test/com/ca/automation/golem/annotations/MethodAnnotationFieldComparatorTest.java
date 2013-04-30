/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.annotations;

import com.ca.automation.golem.annotations.methods.Init;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
public class MethodAnnotationFieldComparatorTest {

    @Rule
    public ExpectedException testException = ExpectedException.none();

    public MethodAnnotationFieldComparatorTest() {
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
     * Test of compare method, of class MethodAnnotationFieldComparator.
     */
    @Test
    public void testCompare00() throws NoSuchMethodException {
        testException.expect(NullPointerException.class);
        testException.expectMessage("Annotation class cannot be null.");
        MethodAnnotationFieldComparator instance = new MethodAnnotationFieldComparator(null, null);
    }

    @Test
    public void testCompare01() throws NoSuchMethodException {
        testException.expect(NullPointerException.class);
        testException.expectMessage("Name of annotation field cannot be null.");
        MethodAnnotationFieldComparator instance = new MethodAnnotationFieldComparator(Init.class, null);
    }

    @Test
    public void testCompare02() throws NoSuchMethodException {
        testException.expect(NoSuchMethodException.class);
        String testName = "jUnitTestField";
//        testException.expectMessage(NoSuchMethodException.class.getName()+": "+Init.class.getName()+"."+testName+"()");
        MethodAnnotationFieldComparator instance = new MethodAnnotationFieldComparator(Init.class, testName);
    }

    @Test
    public void testCompare03() throws NoSuchMethodException {
        String testName = "order";
        MethodAnnotationFieldComparator instance = new MethodAnnotationFieldComparator(Init.class, testName);
        Test01Class obj = new Test01Class();
        Method[] testMethods = obj.getClass().getDeclaredMethods();
        List<Method> tmpList = Arrays.asList(testMethods);
        Collections.sort(tmpList, instance);
        int index = 11;
        for (Method m : tmpList) {
            String s = m.getName();
            String format = String.format("method%02d", index--);
            assertEquals(format, s);
        }
    }

    @RunAction
    private class Test01Class {

        @Init(order = 5)
        public void method00() {
        }

        @Init(order = 4)
        public void method01() {
        }

        @Init(order = 3)
        public void method02() {
        }

        @Init(order = 2)
        public void method03() {
        }

        @Init(order = 1)
        public void method04() {
        }

        @Init(order = 0)
        public void method05() {
        }

        @Init(order = -1)
        public void method06() {
        }

        @Init(order = -2)
        public void method07() {
        }

        @Init(order = -3)
        public void method08() {
        }

        @Init(order = -4)
        public void method09() {
        }

        @Init(order = -5)
        public void method10() {
        }

        @Init
        public void method11() {
        }
    }
}
