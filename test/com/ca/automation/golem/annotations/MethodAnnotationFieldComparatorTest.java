/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.annotations;

import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import com.ca.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingInit;
import com.ca.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingRun;
import com.ca.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingValidate;
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
        /**
         * Declare variables for testing of sorting methods defined in reverse
         * order and numers are in one numeric line. Testing comparator for
         * method annotation Init.
         */
        String testName = "order";
        MethodAnnotationFieldComparator instance = new MethodAnnotationFieldComparator(Init.class, testName);
        ActionWithMethodsForOrderingInit obj = new ActionWithMethodsForOrderingInit();
        Method[] testMethods = obj.getClass().getDeclaredMethods();
        List<Method> tmpList = Arrays.asList(testMethods);
        Collections.sort(tmpList, instance);
        int index = 11;
        for (Method m : tmpList) {
            String s = m.getName();
            String format = String.format("method%02d", index--);
            assertEquals(format, s);
        }

        /**
         * Initialize object for testing sorting of method with Run annotation declared in random order.
         */
        ActionWithMethodsForOrderingRun runObj = new ActionWithMethodsForOrderingRun();
        instance = new MethodAnnotationFieldComparator(Run.class, testName);
        testMethods = runObj.getClass().getDeclaredMethods();
        tmpList = Arrays.asList(testMethods);
        Collections.sort(tmpList, instance);
        index = 0;
        for (Method m : tmpList) {
            String s = m.getName();
            String format = String.format("method%02d", index++);
            assertEquals(format, s);
        }

        /**
         * Initialize and test sorting of method annotatted by Validate annotation and defined in sorded order.
         */
        ActionWithMethodsForOrderingValidate valObj = new ActionWithMethodsForOrderingValidate();
        instance= new MethodAnnotationFieldComparator(Validate.class, testName);
        testMethods = valObj.getClass().getDeclaredMethods();
        tmpList = Arrays.asList(testMethods);
        Collections.sort(tmpList, instance);
        index = 0;
        for (Method m : tmpList) {
            String s = m.getName();
            String format = String.format("method%02d", index++);
            assertEquals(format, s);
        }
        
    }
}
