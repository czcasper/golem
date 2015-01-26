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
package cz.a_d.automation.golem.annotations;

import cz.a_d.automation.golem.annotations.methods.Init;
import cz.a_d.automation.golem.annotations.methods.Run;
import cz.a_d.automation.golem.annotations.methods.Validate;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingInit;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingRun;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingValidate;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author casper
 */
public class MethodAnnotationFieldComparatorTest {

    @Rule
    public ExpectedException testException = ExpectedException.none();

    public MethodAnnotationFieldComparatorTest() {
    }

    /**
     * Test of compare method, of class MethodAnnotationFieldComparator.
     */
    @Test
    public void testCompare00() throws NoSuchMethodException {
        testException.expect(NullPointerException.class);
        testException.expectMessage("Annotation class cannot be null.");
        MethodAnnotationFieldComparator<Init> instance = new MethodAnnotationFieldComparator<>(null, null);
    }

    @Test
    public void testCompare01() throws NoSuchMethodException {
        testException.expect(NullPointerException.class);
        testException.expectMessage("Name of annotation field cannot be null.");
        MethodAnnotationFieldComparator<Init> instance = new MethodAnnotationFieldComparator<>(Init.class, null);
    }

    @Test
    public void testCompare02() throws NoSuchMethodException {
        testException.expect(NoSuchMethodException.class);
        String testName = "jUnitTestField";
        testException.expectMessage(Init.class.getName()+"."+testName+"()");
        MethodAnnotationFieldComparator<Init> instance = new MethodAnnotationFieldComparator<>(Init.class, testName);
    }

    @Test
    public void testCompare03() throws NoSuchMethodException {
        /**
         * Declare variables for testing of sorting methods defined in reverse
         * order and numers are in one numeric line. Testing comparator for
         * method annotation Init.
         */
        String testName = "order";
        MethodAnnotationFieldComparator<Init> instance = new MethodAnnotationFieldComparator<>(Init.class, testName);
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
        MethodAnnotationFieldComparator<Run> runInstance = new MethodAnnotationFieldComparator<>(Run.class, testName);
        testMethods = runObj.getClass().getDeclaredMethods();
        tmpList = Arrays.asList(testMethods);
        Collections.sort(tmpList, runInstance);
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
        MethodAnnotationFieldComparator<Validate> valInstance= new MethodAnnotationFieldComparator<>(Validate.class, testName);
        testMethods = valObj.getClass().getDeclaredMethods();
        tmpList = Arrays.asList(testMethods);
        Collections.sort(tmpList, valInstance);
        index = 0;
        for (Method m : tmpList) {
            String s = m.getName();
            String format = String.format("method%02d", index++);
            assertEquals(format, s);
        }
        
    }
}
