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
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import cz.a_d.automation.testClasses.common.CustomBigIntStringKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author casper
 */
public class SimpleParameterKeyTest {

    protected final static Set<String> testKeys = new HashSet<>();

    @BeforeClass
    public static void beforeTestCase() {
        Random r = new Random();
        int count = r.nextInt(20) + 50;
        byte[] tmp;
        for (int i = 0; i < count; i++) {
            tmp = new byte[r.nextInt(50) + 50];

            /*
             * Fix zero leading bytes in random data. Custom key cannot work correctly with them.
             */
            for (int j = 0; j < tmp.length; j++) {
                if (tmp[j] == 0) {
                    tmp[j]++;
                }
            }
            testKeys.add(new String(tmp));
        }

    }

    @AfterClass
    public static void afterTestCase() {
        testKeys.clear();
    }

    public SimpleParameterKeyTest() {
    }

    /**
     * Test of fromString method, of class SimpleParameterKey.
     */
    @Test
    public void testFromString() {
        /**
         * Initialize and test protection agains null and empty parameter value.
         */
        String key = null;
        SimpleParameterKey instance = new SimpleParameterKey(key);
        boolean result = instance.fromString(key);
        assertFalse(result);
        key = "";
        result = instance.fromString(key);
        assertFalse(result);

        /**
         * Initialize and test instance by random generated strings.
         */
        byte[] tmp;
        Random r = new Random();
        int count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            tmp = new byte[r.nextInt(50) + 50];
            key = new String(tmp);
            result = instance.fromString(key);
            assertTrue(result);
        }
    }

    /**
     * Test of clone method, of class SimpleParameterKey.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testClone() throws Exception {
        /**
         * Test cloning with null key value inside instance.
         */
        String key = null;
        SimpleParameterKey instance = new SimpleParameterKey(key);
        SimpleParameterKey result = (SimpleParameterKey) instance.clone();
        assertNotSame(instance, result);
        assertNull(instance.get());
        assertNull(result.get());

        /**
         * Test cloning with valid key value inside instance.
         */
        key = "Valid Key Value!!";
        instance.fromString(key);
        result = (SimpleParameterKey) instance.clone();
        assertNotSame(instance, result);
        assertEquals(key, result.get());
        assertEquals(key, instance.get());

        /**
         * Test change in clone to be sure clons have saparated memory space for
         * key.
         */
        String secodKey = "This is second key.";
        result.set(secodKey);
        assertEquals(secodKey, result.get());
        assertEquals(key, instance.get());
    }

    /**
     * Test of toString method, of class SimpleParameterKey.
     */
    @Test
    public void testToString() {
        /**
         * Initialize and test protection agains null key valu inside instance.
         */
        SimpleParameterKey instance = new SimpleParameterKey(null);
        String result = instance.toString();
        assertEquals("", result);

        /**
         * Initialize and test instance by randomly genereted string.
         */
        String key;
        byte[] tmp;
        Random r = new Random();
        int count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            tmp = new byte[r.nextInt(50) + 50];
            r.nextBytes(tmp);
            key = new String(tmp);
            boolean set = instance.fromString(key);
            assertTrue(set);
            result = instance.toString();
            assertSame(key, result);
        }
    }

    @Test
    public void testHashCode() {
        /**
         * Initialize and test protection agains null key value inside instance.
         */
        String key = null;
        SimpleParameterKey instance = new SimpleParameterKey(key);
        int result = instance.hashCode();
        assertEquals(0, result);

        /**
         * Initialize and test hashCode validity.
         */
        key = "Valid";
        boolean init = instance.fromString(key);
        assertTrue(init);
        result = instance.hashCode();
        assertEquals(key.hashCode(), result);

        /**
         * Initialize and test situation where multiple instance contains same
         * key.
         */
        Set<AbstractSpoolKey<?>> testDb = new HashSet<>();
        Random r = new Random();
        int count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            instance = new SimpleParameterKey(key);
            testDb.add(instance);
            assertEquals(1, testDb.size());
        }

        /**
         * Initialize and test interaction with correctly created Custom
         * implementation of spool key.
         */
        count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            CustomBigIntStringKey customKey = new CustomBigIntStringKey(null);
            customKey.fromString(key);
            testDb.add(customKey);
            assertEquals(1, testDb.size());
        }

        /**
         * Initialize and test method by set of unique string and two types of
         * key stored inside testDb.
         */
        testDb.clear();
        int expectedSize = testDb.size();

        for (String k : testKeys) {
            AbstractSpoolKey<?> tmpKey;
            if (r.nextBoolean()) {
                tmpKey = new CustomBigIntStringKey(null);
                tmpKey.fromString(k);
            } else {
                tmpKey = new SimpleParameterKey(k);
            }
            boolean add = testDb.add(tmpKey);
            assertTrue(add);
            assertEquals(++expectedSize, testDb.size());
        }
    }

    @Test
    public void testEquals() {
        /**
         * Initialize and test protection agains null instance of compared
         * object.
         */
        String key = null;
        AbstractSpoolKey<?> instance = new SimpleParameterKey(key);
        SimpleParameterKey other = null;
        boolean result = instance.equals(other);
        assertFalse(result);
        other = new SimpleParameterKey(key);
        result = instance.equals(other);
        assertTrue(result);

        /**
         * Initialize and test comparison with empty custom key.
         */
        CustomBigIntStringKey customKey = new CustomBigIntStringKey(null);
        result = instance.equals(customKey);
        assertTrue(result);

        /**
         * Initialize and test equal in Array.
         */
        List<AbstractSpoolKey<?>> testDb = new ArrayList<>();
        int expectedSize = testDb.size();
        for (String s : testKeys) {
            instance = new SimpleParameterKey(s);
            assertFalse(testDb.contains(instance));
            testDb.add(instance);
            assertEquals(++expectedSize, testDb.size());
        }
        /**
         * Test with custom key implementation.
         */
        for (String s : testKeys) {
            instance = new CustomBigIntStringKey(null);
            boolean from = instance.fromString(s);
            assertTrue(from);
            assertTrue(testDb.contains(instance));
        }
    }
}
