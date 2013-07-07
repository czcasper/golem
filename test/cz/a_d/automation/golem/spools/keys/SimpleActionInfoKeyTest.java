/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithAfterInit;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithBeforeRun;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithConnection;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithNamedMembers;
import cz.a_d.automation.testClasses.actions.wrong.WrongActionNoAnnots;
import cz.a_d.automation.testClasses.actions.wrong.WrongActionNoDefCOnstructor;
import cz.a_d.automation.testClasses.common.CustomStringSpoolKey;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author maslu02
 */
public class SimpleActionInfoKeyTest {

    protected static List<Class<?>> testData = new ArrayList<>();

    public SimpleActionInfoKeyTest() {
    }

    @BeforeClass
    public static void beforeTestCase() {
        testData.add(Double.class);
        testData.add(Boolean.class);
        testData.add(Integer.class);
        testData.add(Float.class);
        testData.add(Class.class);
        testData.add(Method.class);
        testData.add(Field.class);
        testData.add(Random.class);
        testData.add(Object.class);
        testData.add(Calendar.class);
        testData.add(ActionWithAfterInit.class);
        testData.add(ActionWithMembers.class);
        testData.add(ActionWithNamedMembers.class);
        testData.add(ActionWithContext.class);
        testData.add(ActionWithConnection.class);
        testData.add(ActionWithBeforeRun.class);
        testData.add(WrongActionNoAnnots.class);
        testData.add(WrongActionNoDefCOnstructor.class);
        testData.add(Package.class);
    }

    /**
     * Test of fromString method, of class SimpleActionInfoKey.
     */
    @Test
    public void testFromString() {
        /**
         * Initialize and test protection agains null and empty parameter value.
         */
        String key = null;
        Class<?> testCl = null;
        SimpleActionInfoKey instance = new SimpleActionInfoKey(testCl);
        boolean result = instance.fromString(key);
        assertFalse(result);
        key = "";
        result = instance.fromString(key);
        assertFalse(result);

        /**
         * Initialize and test protection agains random generated strings.
         */
        Random r = new Random();
        int count = r.nextInt(50) + 50;
        byte[] tmpBytes;
        for (int i = 0; i < count; i++) {
            tmpBytes = new byte[r.nextInt(50) + 20];
            r.nextBytes(tmpBytes);
            key = new String(tmpBytes);
            result = instance.fromString(key);
            assertFalse(result);
        }

        /**
         * Test with valid test data.
         */
        for (Class<?> cl : testData) {
            key = cl.getName();
            result = instance.fromString(key);
            assertTrue(result);
        }
    }

    /**
     * Test of clone method, of class SimpleActionInfoKey.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testClone() throws Exception {
        /**
         * Test cloning with null key value inside instance.
         */
        Class<?> testCl = null;
        SimpleActionInfoKey instance = new SimpleActionInfoKey(testCl);
        SimpleActionInfoKey result = (SimpleActionInfoKey) instance.clone();
        assertNotSame(instance, result);
        assertNull(instance.get());
        assertNull(result.get());

        /**
         * Test cloning with valid key value inside instance.
         */
        testCl = Integer.class;
        instance.set(testCl);
        result = (SimpleActionInfoKey) instance.clone();
        assertNotSame(instance, result);
        assertSame(testCl, instance.get());
        assertSame(testCl, result.get());

        /**
         * Test change in clone to be sure clons have saparated memory space for
         * key.
         */
        Class<?> secondCl = Double.class;
        result.set(secondCl);
        assertSame(secondCl, result.get());
        assertSame(testCl, instance.get());
    }

    /**
     * Test of toString method, of class SimpleActionInfoKey.
     */
    @Test
    public void testToString() {
        /**
         * Initialize and test protection agains null and empty parameter value.
         */
        Class<?> testCl = null;
        SimpleActionInfoKey instance = new SimpleActionInfoKey(testCl);
        String result = instance.toString();
        assertEquals("", result);

        /**
         * Initialize instance by valid data and test method.
         */
        String key;
        for (Class<?> cl : testData) {
            key = cl.getName();
            boolean put = instance.fromString(key);
            assertTrue(put);
            result = instance.toString();
            assertEquals(key, result);
        }
    }

    @Test
    public void testHashCode() {
        /**
         * Initialize and test protection agains null key value inside instance.
         */
        Class<?> testCl = null;
        SimpleActionInfoKey instance = new SimpleActionInfoKey(testCl);
        int result = instance.hashCode();
        assertEquals(0, result);

        /**
         * Initialize and test hashCode validity.
         */
        testCl = Double.class;
        instance.fromString(testCl.getName());
        result = instance.hashCode();
        assertEquals(testCl.getName().hashCode(), result);

        /**
         * Initialize and test situation where multiple instance contains same
         * key.
         */
        Set<AbstractSpoolKey<?>> testDb = new HashSet<>();
        Random r = new Random();
        int count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            instance = new SimpleActionInfoKey(testCl);
            testDb.add(instance);
            assertEquals(1, testDb.size());
        }

        /**
         * Initialize and test interaction with correctly created Custom
         * implementation of spool key.
         */
        String name = testCl.getName();
        count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            CustomStringSpoolKey customKey = new CustomStringSpoolKey(name);
            testDb.add(customKey);
            assertEquals(1, testDb.size());
        }

        /**
         * Test with valid unique test data.
         */
        testDb.clear();
        int expectedSize = testDb.size();
        for (Class<?> cl : testData) {
            AbstractSpoolKey<?> tmpKey;
            if (r.nextBoolean()) {
                tmpKey = new CustomStringSpoolKey(cl.getName());
            } else {
                tmpKey = new SimpleActionInfoKey(cl);
            }
            testDb.add(tmpKey);
            assertEquals(++expectedSize, testDb.size());
        }
    }

    @Test
    public void testEquals() {
        /**
         * Initialize and test protection agains null instance of compared
         * object.
         */
        Class<?> testCl = null;
        AbstractSpoolKey<?> instance = new SimpleActionInfoKey(testCl);
        SimpleActionInfoKey other = null;
        boolean result = instance.equals(other);
        assertFalse(result);
        other = new SimpleActionInfoKey(testCl);
        result = instance.equals(other);
        assertTrue(result);

        /**
         * Initialize and test comparison with empty custom key.
         */
        CustomStringSpoolKey customKey = new CustomStringSpoolKey(null);
        result = instance.equals(customKey);
        assertTrue(result);

        /**
         * Initialize and test equal in Array.
         */
        List<AbstractSpoolKey<?>> testDb = new ArrayList<>();
        int expectedSize = testDb.size();
        for (Class<?> cl : testData) {
            instance = new SimpleActionInfoKey(cl);
            assertFalse(testDb.contains(instance));
            testDb.add(instance);
            assertEquals(++expectedSize, testDb.size());
        }

        /**
         * Test with custom key implementation.
         */
        for (Class<?> cl : testData) {
            instance = new CustomStringSpoolKey(cl.getName());
            assertTrue(testDb.contains(instance));
        }
    }
}
