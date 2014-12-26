/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.spools.ActionInformationSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import cz.a_d.automation.golem.spools.actions.ActionInfoProxyImpl;
import cz.a_d.automation.golem.spools.enums.ActionMethodProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleActionInfoKey;
import cz.a_d.automation.testClasses.actions.dummy.ActionWithInit;
import cz.a_d.automation.testClasses.actions.dummy.ActionWithValidate;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithAfterInit;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithAfterValidate;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithBeforeInit;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithBeforeValidate;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingInit;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithMethodsForOrderingValidate;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionForTestingContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithConnection;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithNamedMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithPointerToContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithPointersOnMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithRetValues;
import cz.a_d.automation.testClasses.actions.dummy.valid.SimpleValidAction;
import cz.a_d.automation.testClasses.actions.dummy.valid.ValidActionWithNoRun;
import cz.a_d.automation.testClasses.actions.wrong.WrongActionNoAnnots;
import cz.a_d.automation.testClasses.actions.wrong.WrongActionNoDefCOnstructor;
import cz.a_d.automation.testClasses.actions.wrong.WrongNoRunMethod;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author casper
 */
public class ActionInformationSpoolImplTest {

    @Rule
    public ExpectedException testException = ExpectedException.none();

    public ActionInformationSpoolImplTest() {
    }

    /**
     * Test of getGlobal method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGetGlobal() {
        /**
         * Declare and initialize variables for testing functionality of static
         * method getGlobal.
         */
        ActionInformationSpool<Object> result = ActionInformationSpoolImpl.getGlobal();
        assertNotNull(result);

        ActionInfoProxy testProxy = new ActionInfoProxyImpl(ActionInfoProxyImpl.createNewComparators());
        testProxy.loadAction(ActionWithMembers.class, result);

        /**
         * Test simple common use case scenarion for global spool. Store value
         * in one instace and retrive from instance initialized by another
         * getGlobal call.
         */
        result.clear();
        assertTrue(result.isEmpty());
        String className = Integer.class.getName();
        result.putFrom(className, testProxy);
        assertFalse(result.isEmpty());
        result = null;
        assertNull(result);
        result = ActionInformationSpoolImpl.getGlobal();
        assertFalse(result.isEmpty());
        ActionInfoProxy get = result.getFrom(className);
        assertSame(testProxy, get);
        result.clear();
    }

    /**
     * Test of newInstance method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testNewInstance() {
        /**
         * Initialize variables for testing creation of new instance from
         * existing one.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        ActionInfoProxyImpl proxy = new ActionInfoProxyImpl(ActionInfoProxyImpl.createNewComparators());
        proxy.loadAction(ActionWithMembers.class, instance);
        instance.put(new SimpleActionInfoKey(Integer.class), proxy);
        assertFalse(instance.isEmpty());

        /**
         * Test if new instance is created without fields and independen on
         * parent instance.
         */
        ActionInformationSpool<Object> result = instance.newInstance();
        assertNotNull(result);
        assertNotSame(instance, result);
        assertTrue(result.isEmpty());
        result.clear();
        assertFalse(instance.isEmpty());
    }

    /**
     * Test of isValidAction method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testIsValidAction_Object() {
        /**
         * Initialize and test protection agains null parameter value.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        Object action = null;
        boolean result = instance.isValidAction(action);
        assertFalse(result);

        /**
         * Initializing variables for testing cycle.
         */
        Object[] valid = {new ActionForTestingContext<>(), new ActionWithConnection(), new ActionWithContext(), new ActionWithMembers(), new ActionWithNamedMembers(), new ActionWithPointerToContext(), new ActionWithPointersOnMembers(), new ActionWithRetValues(), new SimpleValidAction(), new ValidActionWithNoRun()};
        Object[] invalid = {new ActionWithInit(), new ActionWithValidate(), new ActionWithAfterInit(), new ActionWithAfterValidate(), new ActionWithBeforeInit(), new ActionWithBeforeValidate(), new ActionWithMethodsForOrderingInit(), new ActionWithMethodsForOrderingValidate(), new WrongActionNoAnnots(), new WrongActionNoDefCOnstructor(Integer.SIZE), new WrongNoRunMethod()};
        Iterator<Object> validIt = Arrays.asList(valid).iterator();
        Iterator<Object> invalidIt = Arrays.asList(invalid).iterator();
        Random r = new Random();
        int currentSize = instance.size();

        /**
         * Testing method with valid and invalid actions and validate response
         * and expected spool grow in case when action is valid.
         */
        while (validIt.hasNext()) {

            boolean validTest = true;
            if (invalidIt.hasNext()) {
                if (r.nextBoolean()) {
                    action = validIt.next();
                } else {
                    validTest = false;
                    action = invalidIt.next();
                }
            } else {
                action = validIt.next();
            }

            if (r.nextBoolean()) {
                action = action.getClass();
            }
            result = instance.isValidAction(action);
            if (validTest) {
                assertTrue(result);
                assertEquals(++currentSize, instance.size());
            } else {
                assertFalse(result);
                assertEquals(currentSize, instance.size());
            }
        }
        while (invalidIt.hasNext()) {
            result = instance.isValidAction(invalidIt.next());
            assertFalse(result);
        }

        /**
         * Test protection agains duplication of valid classes in spool.
         */
        validIt = Arrays.asList(valid).iterator();
        action = validIt.next();
        currentSize = instance.size();
        while (validIt.hasNext()) {
            result = instance.isValidAction(action);
            assertTrue(result);
            assertEquals(currentSize, instance.size());
            if (r.nextBoolean()) {
                action = validIt.next();
            }
        }
    }

    /**
     * Test of isValidAction method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testIsValidAction_Class() {
        /**
         * Initialize and test protection agains null parameter value.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        Class<?> action = null;
        boolean result = instance.isValidAction(action);
        assertFalse(result);

        /**
         * Initializing variables for testing cycle.
         */
        List<Class<?>> valid = new ArrayList<>();
        valid.add(ActionForTestingContext.class);
        valid.add(ActionWithConnection.class);
        valid.add(ActionWithContext.class);
        valid.add(ActionWithMembers.class);
        valid.add(ActionWithNamedMembers.class);
        valid.add(ActionWithPointerToContext.class);
        valid.add(ActionWithPointersOnMembers.class);
        valid.add(ActionWithRetValues.class);
        valid.add(SimpleValidAction.class);
        valid.add(ValidActionWithNoRun.class);

        List<Class<?>> invalid = new ArrayList<>();
        invalid.add(ActionWithInit.class);
        invalid.add(ActionWithValidate.class);
        invalid.add(ActionWithAfterInit.class);
        invalid.add(ActionWithAfterValidate.class);
        invalid.add(ActionWithBeforeInit.class);
        invalid.add(ActionWithBeforeValidate.class);
        invalid.add(ActionWithMethodsForOrderingInit.class);
        invalid.add(ActionWithMethodsForOrderingValidate.class);
        invalid.add(WrongActionNoAnnots.class);
        invalid.add(WrongActionNoDefCOnstructor.class);
        invalid.add(WrongNoRunMethod.class);

        Iterator<Class<?>> validIt = valid.iterator();
        Iterator<Class<?>> invalidIt = invalid.iterator();
        Random r = new Random();
        int currentSize = instance.size();
        /**
         * Testing method with valid and invalid actions and validate response
         * and expected spool grow in case when action is valid.
         */
        while (validIt.hasNext()) {

            boolean validTest = true;
            if (invalidIt.hasNext()) {
                if (r.nextBoolean()) {
                    action = validIt.next();
                } else {
                    validTest = false;
                    action = invalidIt.next();
                }
            } else {
                action = validIt.next();
            }

            result = instance.isValidAction(action);
            if (validTest) {
                assertTrue(result);
                assertEquals(++currentSize, instance.size());
            } else {
                assertFalse(result);
                assertEquals(currentSize, instance.size());
            }
        }
        while (invalidIt.hasNext()) {
            result = instance.isValidAction(invalidIt.next());
            assertFalse(result);
        }

        /**
         * Test protection agains duplication of valid classes in spool.
         */
        validIt = valid.iterator();
        action = validIt.next();
        currentSize = instance.size();
        while (validIt.hasNext()) {
            result = instance.isValidAction(action);
            assertTrue(result);
            assertEquals(currentSize, instance.size());
            if (r.nextBoolean()) {
                action = validIt.next();
            }
        }
    }

    /**
     * Test of createNewFromObject method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testCreateNewFromObject() {
        /**
         * Initialize and test protection agains null parameter values and empty
         * list.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        List<Object> testData = null;
        ActionStream<Object, Object> result = instance.createNewFromObject(testData);
        assertNull(result);
        testData = new ArrayList<>();
        result = instance.createNewFromObject(testData);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test protection agains list of wrong type of object
         * (not valid actions).
         */
        Object[] invalid = {new ActionWithInit(), new ActionWithValidate(), new ActionWithAfterInit(), new ActionWithAfterValidate(), new ActionWithBeforeInit(), new ActionWithBeforeValidate(), new ActionWithMethodsForOrderingInit(), new ActionWithMethodsForOrderingValidate(), new WrongActionNoAnnots(), new WrongActionNoDefCOnstructor(Integer.SIZE), new WrongNoRunMethod()};
        testData = Arrays.asList(invalid);
        result = instance.createNewFromObject(testData);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test valid list of action.
         */
        Object[] valid = {new ActionForTestingContext<>(), new ActionWithConnection(), new ActionWithContext(), new ActionWithMembers(), new ActionWithNamedMembers(), new ActionWithPointerToContext(), new ActionWithPointersOnMembers(), new ActionWithRetValues(), new SimpleValidAction(), new ValidActionWithNoRun()};
        testData = Arrays.asList(valid);
        result = instance.createNewFromObject(testData);
        assertNotNull(result);
        assertEquals(valid.length, instance.size());
        assertArrayEquals(valid, result.getActionList().toArray());

        /**
         * Initialize and test edge use case with mixed object and class.
         */
        Object[] validWitCl = {ActionForTestingContext.class, new ActionWithConnection(), new ActionWithContext(), new ActionWithMembers(), ActionWithNamedMembers.class, new ActionWithPointerToContext(), new ActionWithPointersOnMembers(), ActionWithRetValues.class, new SimpleValidAction(), ValidActionWithNoRun.class};
        testData = Arrays.asList(validWitCl);
        result = instance.createNewFromObject(testData);
        assertNotNull(result);
        assertEquals(validWitCl.length, instance.size());
        Iterator<Object> valIt = Arrays.asList(valid).iterator();
        Iterator<Object> resIt = result.getActionList().iterator();
        while (resIt.hasNext()) {
            assertEquals(valIt.next().getClass(), resIt.next().getClass());
        }
    }

    /**
     * Test of createNewFromClasses method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testCreateNewFromClasses() {
        /**
         * Initialize and test protection agains null parameter values and empty
         * list.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        List<Class<?>> testData = null;
        ActionStream<Object, Object> result = instance.createNewFromClasses(testData);
        assertNull(result);
        testData = new ArrayList<>();
        result = instance.createNewFromClasses(testData);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test protection agains list of wrong type of object
         * (not valid actions).
         */
        List<Class<?>> invalid = new ArrayList<>();
        invalid.add(ActionWithInit.class);
        invalid.add(ActionWithValidate.class);
        invalid.add(ActionWithAfterInit.class);
        invalid.add(ActionWithAfterValidate.class);
        invalid.add(ActionWithBeforeInit.class);
        invalid.add(ActionWithBeforeValidate.class);
        invalid.add(ActionWithMethodsForOrderingInit.class);
        invalid.add(ActionWithMethodsForOrderingValidate.class);
        invalid.add(WrongActionNoAnnots.class);
        invalid.add(WrongActionNoDefCOnstructor.class);
        invalid.add(WrongNoRunMethod.class);
        result = instance.createNewFromClasses(invalid);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test valid list of action.
         */
        List<Class<?>> valid = new ArrayList<>();
        valid.add(ActionForTestingContext.class);
        valid.add(ActionWithConnection.class);
        valid.add(ActionWithContext.class);
        valid.add(ActionWithMembers.class);
        valid.add(ActionWithNamedMembers.class);
        valid.add(ActionWithPointerToContext.class);
        valid.add(ActionWithPointersOnMembers.class);
        valid.add(ActionWithRetValues.class);
        valid.add(SimpleValidAction.class);
        valid.add(ValidActionWithNoRun.class);
        result = instance.createNewFromClasses(valid);
        assertNotNull(result);
        assertEquals(valid.size(), instance.size());
        Iterator<Class<?>> valIt = valid.iterator();
        Iterator<Object> resIt = result.getActionList().iterator();
        while (resIt.hasNext()) {
            assertEquals(valIt.next(), resIt.next().getClass());
        }
    }

    /**
     * Test of containsFrom method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testContainsFrom_Object() throws InstantiationException, IllegalAccessException {
        /**
         * Initialize and test protection agains null parameter value.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        Object key = null;
        boolean result = instance.containsFrom(key);
        assertFalse(result);

        /**
         * Initialize instance by data for following test.
         */
        List<Class<?>> valid = new ArrayList<>();
        valid.add(ActionForTestingContext.class);
        valid.add(ActionWithConnection.class);
        valid.add(ActionWithContext.class);
        valid.add(ActionWithMembers.class);
        valid.add(ActionWithNamedMembers.class);
        valid.add(ActionWithPointerToContext.class);
        valid.add(ActionWithPointersOnMembers.class);
        valid.add(ActionWithRetValues.class);
        valid.add(SimpleValidAction.class);
        valid.add(ValidActionWithNoRun.class);

        for (Class<?> cl : valid) {
            result = instance.isValidAction(cl);
            assertTrue(result);
        }
        assertEquals(valid.size(), instance.size());

        /**
         * Test random searching in instance by using various object types.
         */
        Random r = new Random();
        int max = r.nextInt(50) + 50;
        int index, size = valid.size();
        boolean failTest;
        for (int i = 0; i < max; i++) {
            index = r.nextInt(size);
            Class<?> testData = valid.get(index);
            failTest = false;
            Object tmpObj = testData;
            if (r.nextBoolean()) {
                tmpObj = testData.newInstance();
            } else if (r.nextBoolean()) {
                tmpObj = testData.getName();
            } else if (r.nextBoolean()) {
                tmpObj = new SimpleActionInfoKey(testData);
            } else if (r.nextBoolean()) {
                byte[] charArr = new byte[r.nextInt(20) + 5];
                r.nextBytes(charArr);
                tmpObj = new String(charArr);
                failTest = true;
            }
            result = instance.containsFrom(tmpObj);
            if (failTest) {
                assertFalse(result);
            } else {
                assertTrue(result);
            }


        }
        assertEquals(valid.size(), instance.size());
    }

    /**
     * Test of containsFrom method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testContainsFrom_String() throws InstantiationException, IllegalAccessException {
        /**
         * Initialize and test protection agains null parameter value.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        String key = null;
        boolean result = instance.containsFrom(key);
        assertFalse(result);

        /**
         * Initialize instance by data for following test.
         */
        List<Class<?>> valid = new ArrayList<>();
        valid.add(ActionForTestingContext.class);
        valid.add(ActionWithConnection.class);
        valid.add(ActionWithContext.class);
        valid.add(ActionWithMembers.class);
        valid.add(ActionWithNamedMembers.class);
        valid.add(ActionWithPointerToContext.class);
        valid.add(ActionWithPointersOnMembers.class);
        valid.add(ActionWithRetValues.class);
        valid.add(SimpleValidAction.class);
        valid.add(ValidActionWithNoRun.class);

        for (Class<?> cl : valid) {
            result = instance.isValidAction(cl);
            assertTrue(result);
        }
        assertEquals(valid.size(), instance.size());
        /**
         * Test random searching in instance by using various object types.
         */
        Random r = new Random();
        int max = r.nextInt(50) + 50;
        int index, size = valid.size();
        boolean failTest = true;
        key = "";
        for (int i = 0; i < max; i++) {
            index = r.nextInt(size);
            Class<?> testData = valid.get(index);
            if (r.nextBoolean()) {
                key = testData.getName();
                failTest = false;
            } else if (r.nextBoolean()) {
                byte[] tmp = new byte[r.nextInt(50) + 1];
                r.nextBytes(tmp);
                key = new String(tmp);
                failTest = true;
            }
            result = instance.containsFrom(key);
            if (failTest) {
                assertFalse(result);
            } else {
                assertTrue(result);
            }

        }
        assertEquals(valid.size(), instance.size());
    }

    /**
     * Test of getFrom method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGetFrom_String() {
        /**
         * Initialize and test protection agains null and empty parameter
         * values.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        assertTrue(instance.isEmpty());
        String key = "";
        ActionInfoProxy result = instance.getFrom(key);
        assertNull(result);
        key = "";
        result = instance.getFrom(key);
        assertNull(result);

        /**
         * Initialize variables for following testing.
         */
        List<Class<?>> valid = new ArrayList<>();
        valid.add(ActionForTestingContext.class);
        valid.add(ActionWithConnection.class);
        valid.add(ActionWithContext.class);
        valid.add(ActionWithMembers.class);
        valid.add(ActionWithNamedMembers.class);
        valid.add(ActionWithPointerToContext.class);
        valid.add(ActionWithPointersOnMembers.class);
        valid.add(ActionWithRetValues.class);
        valid.add(SimpleValidAction.class);
        valid.add(ValidActionWithNoRun.class);

        int expectedSize = instance.size();
        Iterator<Class<?>> iterator = valid.iterator();
        Random r = new Random();

        /**
         * Testing getting and filling spool by valid actions.
         */
        key = iterator.next().getName();
        boolean expectGrow = true;
        while (iterator.hasNext()) {

            if ((!expectGrow) && (r.nextBoolean())) {
                key = iterator.next().getName();
                expectGrow = true;
            }
            result = instance.getFrom(key);
            assertNotNull(result);
            if (expectGrow) {
                expectedSize++;
            }
            expectGrow = false;
            assertEquals(expectedSize, instance.size());
        }
        assertEquals(valid.size(), instance.size());

        /**
         * Initialize variables for following testing.
         */
        List<Class<?>> invalid = new ArrayList<>();
        invalid.add(ActionWithInit.class);
        invalid.add(ActionWithValidate.class);
        invalid.add(ActionWithAfterInit.class);
        invalid.add(ActionWithAfterValidate.class);
        invalid.add(ActionWithBeforeInit.class);
        invalid.add(ActionWithBeforeValidate.class);
        invalid.add(ActionWithMethodsForOrderingInit.class);
        invalid.add(ActionWithMethodsForOrderingValidate.class);
        invalid.add(WrongActionNoAnnots.class);
        invalid.add(WrongActionNoDefCOnstructor.class);
        invalid.add(WrongNoRunMethod.class);
        iterator = invalid.iterator();
        expectedSize = instance.size();

        /**
         * Test protection agains invalid actions.
         */
        key = Integer.class.getName();
        while (iterator.hasNext()) {
            if (r.nextBoolean()) {
                key = iterator.next().getName();
            }
            result = instance.getFrom(key);
            assertNull(result);
            assertEquals(expectedSize, instance.size());
        }
    }

    /**
     * Test of getFrom method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGetFrom_Object() {
        /**
         * Initialize and test protection agains null parameter value.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        Object key = null;
        ActionInfoProxy result = instance.getFrom(key);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test method by using invalid object instances.
         */
        Object[] invalid = {new String(), new Integer(1), new ActionWithInit(), new ActionWithValidate(), new ActionWithAfterInit(),
            new ActionWithAfterValidate(), new ActionWithBeforeInit(), new ActionWithBeforeValidate(), new ActionWithMethodsForOrderingInit(),
            new ActionWithMethodsForOrderingValidate(), new WrongActionNoAnnots(), new WrongActionNoDefCOnstructor(Integer.SIZE), new WrongNoRunMethod()};
        for (Object action : invalid) {
            result = instance.getFrom(action);
            assertNull(result);
            assertTrue(instance.isEmpty());
        }

        /**
         * Initialize and test method by using valid instances of action object
         * and class mixed in one array.
         */
        Object[] validWitCl = {ActionForTestingContext.class, new ActionWithConnection(), new ActionWithContext(), new ActionWithMembers(), ActionWithNamedMembers.class, new ActionWithPointerToContext(), new ActionWithPointersOnMembers(), ActionWithRetValues.class, new SimpleValidAction(), ValidActionWithNoRun.class};
        int expectedSize = instance.size();
        for (Object action : validWitCl) {
            result = instance.getFrom(action);
            assertNotNull(result);
            assertEquals(++expectedSize, instance.size());
        }
        assertEquals(validWitCl.length, instance.size());
        /**
         * Initialize and test both previous variant in random distribution.
         */
        Iterator<Object> valIt = Arrays.asList(validWitCl).iterator();
        Iterator<Object> notValIt = Arrays.asList(invalid).iterator();
        Random r = new Random();

        key = valIt.next();
        boolean validTest = true;
        while (notValIt.hasNext()) {
            if ((valIt.hasNext()) && (r.nextBoolean())) {
                key = valIt.next();
                validTest = true;
            } else if (r.nextBoolean()) {
                key = notValIt.next();
                validTest = false;
            }
            result = instance.getFrom(key);
            if (validTest) {
                assertNotNull(result);
            } else {
                assertNull(result);
            }
            assertEquals(validWitCl.length, instance.size());
        }
    }

    /**
     * Test of putFrom method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testPutFrom_String() throws Exception {
        /**
         * Initialize and test protection agains null parameter values and
         * invalid proxy instance.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        String key = null;
        ActionInfoProxy value = null;
        ActionInfoProxy result = instance.putFrom(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        value = new ActionInfoProxyImpl(ActionInfoProxyImpl.createNewComparators());
        result = instance.putFrom(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        key = ActionWithMembers.class.getName();
        result = instance.putFrom(key, null);
        assertNull(result);
        assertTrue(instance.isEmpty());

        result = instance.putFrom(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        value.loadAction(ActionWithMembers.class, instance);
        result = instance.putFrom("", value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Test inserting first valid rscord into spool.
         */
        result = instance.putFrom(key, value);
        assertNull(result);
        assertFalse(instance.isEmpty());
        assertEquals(1, instance.size());

        /**
         * Initialize instance by data for following test.
         */
        List<Class<?>> valid = new ArrayList<>();
        valid.add(ActionForTestingContext.class);
        valid.add(ActionWithConnection.class);
        valid.add(ActionWithContext.class);
        valid.add(ActionWithNamedMembers.class);
        valid.add(ActionWithPointerToContext.class);
        valid.add(ActionWithPointersOnMembers.class);
        valid.add(ActionWithRetValues.class);
        valid.add(SimpleValidAction.class);
        valid.add(ValidActionWithNoRun.class);

        List<Class<?>> invalid = new ArrayList<>();
        invalid.add(ActionWithInit.class);
        invalid.add(ActionWithValidate.class);
        invalid.add(ActionWithAfterInit.class);
        invalid.add(ActionWithAfterValidate.class);
        invalid.add(ActionWithBeforeInit.class);
        invalid.add(ActionWithBeforeValidate.class);
        invalid.add(ActionWithMethodsForOrderingInit.class);
        invalid.add(ActionWithMethodsForOrderingValidate.class);
        invalid.add(WrongActionNoAnnots.class);
        invalid.add(WrongActionNoDefCOnstructor.class);
        invalid.add(WrongNoRunMethod.class);

        /**
         * Test method by using valid and invalid action in random distribution.
         */
        int expectedSize = instance.size();
        Iterator<Class<?>> valIt = valid.iterator();
        Iterator<Class<?>> notValIt = invalid.iterator();
        Random r = new Random();
        Class<?> currClass = notValIt.next();
        key = currClass.getName();
        boolean failTest = true, isNew = false;
        Map<ActionMethodProxyType, Comparator<Method>> comparators = ActionInfoProxyImpl.createNewComparators();

        while ((valIt.hasNext()) || (notValIt.hasNext())) {

            if ((r.nextBoolean()) && (valIt.hasNext())) {
                currClass = valIt.next();
                key = currClass.getName();
                isNew = true;
                failTest = false;
            } else if ((r.nextBoolean()) && (notValIt.hasNext())) {
                currClass = notValIt.next();
                key = currClass.getName();
                failTest = true;
            }

            value = new ActionInfoProxyImpl(comparators);
            value.loadAction(currClass, instance);
            result = instance.putFrom(key, value);
            if (failTest) {
                assertNull(result);
                assertEquals(expectedSize, instance.size());
            } else {
                if (isNew) {
                    assertNull(result);
                    assertEquals(++expectedSize, instance.size());
                    isNew = false;
                } else {
                    assertEquals(value, result);
                    assertEquals(expectedSize, instance.size());
                }
            }
        }
        assertEquals(expectedSize, instance.size());
    }

    /**
     * Test of put method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testPut() throws Exception {
        /**
         * Initialize and test protection agains null parameter values and
         * invalid proxy instance.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        ActionInfoKey<Class<?>> key = null;
        ActionInfoProxy value = null;
        ActionInfoProxy result = instance.put(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        value = new ActionInfoProxyImpl(ActionInfoProxyImpl.createNewComparators());
        result = instance.put(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        key = new SimpleActionInfoKey(ActionWithMembers.class);
        result = instance.put(key, null);
        assertNull(result);
        assertTrue(instance.isEmpty());

        result = instance.put(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        value.loadAction(ActionWithMembers.class, instance);
        result = instance.put(null, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize instance by data for following test.
         */
        List<Class<?>> valid = new ArrayList<>();
        valid.add(ActionForTestingContext.class);
        valid.add(ActionWithConnection.class);
        valid.add(ActionWithContext.class);
        valid.add(ActionWithNamedMembers.class);
        valid.add(ActionWithPointerToContext.class);
        valid.add(ActionWithPointersOnMembers.class);
        valid.add(ActionWithRetValues.class);
        valid.add(SimpleValidAction.class);
        valid.add(ValidActionWithNoRun.class);

        List<Class<?>> invalid = new ArrayList<>();
        invalid.add(ActionWithInit.class);
        invalid.add(ActionWithValidate.class);
        invalid.add(ActionWithAfterInit.class);
        invalid.add(ActionWithAfterValidate.class);
        invalid.add(ActionWithBeforeInit.class);
        invalid.add(ActionWithBeforeValidate.class);
        invalid.add(ActionWithMethodsForOrderingInit.class);
        invalid.add(ActionWithMethodsForOrderingValidate.class);
        invalid.add(WrongActionNoAnnots.class);
        invalid.add(WrongActionNoDefCOnstructor.class);
        invalid.add(WrongNoRunMethod.class);

        /**
         * Test method by using valid and invalid action in random distribution.
         */
        int expectedSize = instance.size();
        Iterator<Class<?>> valIt = valid.iterator();
        Iterator<Class<?>> notValIt = invalid.iterator();
        Random r = new Random();
        Class<?> currClass = notValIt.next();
        key = new SimpleActionInfoKey(currClass);
        boolean failTest = true, isNew = false;
        Map<ActionMethodProxyType, Comparator<Method>> comparators = ActionInfoProxyImpl.createNewComparators();

        while ((valIt.hasNext()) || (notValIt.hasNext())) {

            if ((r.nextBoolean()) && (valIt.hasNext())) {
                currClass = valIt.next();
                key = new SimpleActionInfoKey(currClass);
                isNew = true;
                failTest = false;
            } else if ((r.nextBoolean()) && (notValIt.hasNext())) {
                currClass = notValIt.next();
                key = new SimpleActionInfoKey(currClass);
                failTest = true;
            }

            value = new ActionInfoProxyImpl(comparators);
            value.loadAction(currClass, instance);
            result = instance.put(key, value);
            if (failTest) {
                assertNull(result);
                assertEquals(expectedSize, instance.size());
            } else {
                if (isNew) {
                    assertNull(result);
                    assertEquals(++expectedSize, instance.size());
                    isNew = false;
                } else {
                    assertEquals(value, result);
                    assertEquals(expectedSize, instance.size());
                }
            }
        }
        assertEquals(expectedSize, instance.size());
    }

    /**
     * Test of testGet_3args method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGet_3args() throws Exception {
        /**
         * Initialize variables for testing exception in this method.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        Object action = new ActionWithMembers();
        Field field = ActionWithMembers.class.getDeclaredField("text");
        testException.expect(IllegalAccessException.class);
        testException.expectMessage("Action information spool doesn't support direct action interaction with action fields");
        instance.get(action, field, null);
    }

    /**
     * Test of testPut_3args method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testPut_3args() throws Exception {
        /**
         * Initialize variables for testing exception in this method.
         */
        ActionInformationSpool<Object> instance = new ActionInformationSpoolImpl<>();
        testException.expect(IllegalAccessException.class);
        testException.expectMessage("Action information spool doesn't support direct action interaction with action fields");
        instance.put(null, null, null);
    }
}
