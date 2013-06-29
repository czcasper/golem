/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.spools.ParameterSpool;
import com.ca.automation.golem.interfaces.spools.keys.AbstractSpoolKey;
import com.ca.automation.golem.interfaces.spools.keys.ParameterKey;
import com.ca.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import com.ca.automation.testClasses.actions.dummy.valid.ActionWithNamedMembers;
import com.ca.automation.testClasses.actions.dummy.valid.ActionWithPointerToContext;
import com.ca.automation.testClasses.actions.dummy.valid.ActionWithPointersOnMembers;
import com.ca.automation.golem.spools.keys.AbstractSpoolKeyImpl;
import com.ca.automation.golem.spools.keys.SimpleParameterKey;
import com.ca.automation.testClasses.common.CloneableObject;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author maslu02
 */
// TODO Documentation: Create javadoc on class level and methods. For methods javadoc use comments inside methods to describe all test step inside method.
public class ParameterSpoolImplTest {

    public ParameterSpoolImplTest() {
    }

    /**
     * For purpose of testing define CustomKey based on AbstractSpoolKeyImpl
     * class and test functionality.
     */
    class CustomKey extends AbstractSpoolKeyImpl<BigInteger> implements ParameterKey<BigInteger> {

        public CustomKey(BigInteger keyValue) {
            super(keyValue);
        }

        @Override
        public String toString() {
            return keyValue.toString();
        }

        @Override
        public boolean fromString(String key) {
            if ((key == null) || (!key.isEmpty())) {
                return false;
            }
            keyValue = new BigInteger(key);
            return true;
        }
    }

    /**
     * Test of newInstance method, of class ParameterSpoolImpl.
     */
    @Test
    public void testNewInstance() {
        /**
         * Initialize instance for testing and add data inside.
         */
        ParameterSpool<Object, Object> instance = new ParameterSpoolImpl<>();
        Object put = instance.put("test", null);
        assertNull(put);
        assertFalse(instance.isEmpty());

        /**
         * Test creation of new clean instance from existing instance.
         */
        ParameterSpool<Object, Object> result = instance.newInstance();
        assertNotSame(instance, result);
        assertFalse(instance.isEmpty());
        assertTrue(result.isEmpty());

        /**
         * Test creation of seconf new clean instance from existing one.
         */
        ParameterSpool<Object, Object> result2 = instance.newInstance();
        assertNotSame(instance, result);
        assertNotSame(instance, result2);
        assertNotSame(result, result2);
        assertFalse(instance.isEmpty());
        assertTrue(result.isEmpty());
        assertTrue(result2.isEmpty());
    }

    /**
     * Test of put method functionality.
     */
    @Test
    public void testPut_String_GenericType() {
        /**
         * Initialize testing instance and variables for first test.
         */
        ParameterSpool<Object, Object> instance = new ParameterSpoolImpl<>();
        String strKey = null;
        Integer parameter = null;

        /**
         * Testing protection agains null and empty parameter key.
         */
        Object put = instance.put(strKey, parameter);
        assertNull(put);
        assertTrue(instance.isEmpty());
        strKey = "";
        put = instance.put(strKey, parameter);
        assertNull(put);
        assertTrue(instance.isEmpty());

        /**
         * Test inserting valid value into map.
         */
        strKey = "test";
        parameter = 1;
        put = instance.put(strKey, parameter);
        assertNull(put);
        assertFalse(instance.isEmpty());

        /**
         * Validate insertion by search value using key implementaton.
         */
        SimpleParameterKey key = new SimpleParameterKey(strKey);
        Object get = instance.get(key);
        assertSame(parameter, get);

        /**
         * Test value overwriting.
         */
        Double parameter2 = 2.5;
        put = instance.put(strKey, parameter2);
        assertFalse(instance.isEmpty());
        assertSame(parameter, put);
        assertEquals(1, instance.size());
        get = instance.get(key);
        assertSame(parameter2, get);
    }

    /**
     * Test of put method, with parameters A action, Field f,
     * ParameterSpool<A,P> parameters parameters.
     */
    @Test
    public void testPut_3args() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        /**
         * Initialize testing instance and variables for first test.
         */
        ParameterSpool<Object, Object> instance = new ParameterSpoolImpl<>();
        Field field = null;
        ActionWithMembers action = null;
        assertTrue(instance.isEmpty());

        /**
         * Test protection aganis all combination of null parameters value.
         */
        Object result = instance.put(action, field, null);
        assertNull(result);
        assertTrue(instance.isEmpty());

        action = new ActionWithMembers();
        result = instance.put(action, field, null);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize field from tested class and set field accessible by
         * reflection.
         */
        field = ActionWithMembers.class.getDeclaredField("text");
        field.setAccessible(true);

        result = instance.put(null, field, null);
        assertNull(result);
        assertTrue(instance.isEmpty());

        result = instance.put(null, field, instance);
        assertNull(result);
        assertTrue(instance.isEmpty());

        result = instance.put(action, null, instance);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Testin putting first valid fields with pointer feature off.
         */
        result = instance.put(action, field, null);
        assertNull(result);
        assertFalse(instance.isEmpty());
        assertEquals(1, instance.size());
        assertTrue(instance.contains(ActionWithMembers.class.getName() + "." + field.getName()));
        assertSame(instance.get(ActionWithMembers.class.getName() + "." + field.getName()), action.getText());

        /**
         * Test replacing values stored in map by new value from object field.
         */
        String expected = action.getText();
        action.setText("Hello world test");
        result = instance.put(action, field, null);
        assertSame(expected, result);
        assertFalse(instance.isEmpty());
        assertEquals(1, instance.size());
        assertTrue(instance.contains(ActionWithMembers.class.getName() + "." + field.getName()));
        assertSame(instance.get(ActionWithMembers.class.getName() + "." + field.getName()), action.getText());

        /**
         * Test putting parameter with defined pointer value and non existing
         * key pointer.
         */
        instance.clear();
        field = ActionWithPointersOnMembers.class.getDeclaredField("test");
        field.setAccessible(true);
        ActionWithPointersOnMembers action2 = new ActionWithPointersOnMembers();
        action2.setTest("Hello");
        result = instance.put(action2, field, instance);
        assertNull(result);
        assertFalse(instance.isEmpty());
        assertEquals(1, instance.size());
        assertTrue(instance.contains(ActionWithPointersOnMembers.class.getName() + "." + field.getName()));
        assertSame(instance.get(ActionWithPointersOnMembers.class.getName() + "." + field.getName()), action2.getTest());

        /**
         * Initialize and test pointer feature with string type of value used by
         * pointer key.
         */
        SimpleParameterKey ptrKey = new SimpleParameterKey("1");
        String ptrValue = "test";
        instance.put(ptrKey, ptrValue);
        expected = action2.getTest();
        action2.setTest("Hello ptr one");
        result = instance.put(action2, field, instance);
        assertNull(result);
        assertFalse(instance.isEmpty());
        assertEquals(3, instance.size());
        assertTrue(instance.contains(ptrValue));
        assertSame(action2.getTest(), instance.get(ptrValue));
        assertTrue(instance.contains(ActionWithPointersOnMembers.class.getName() + "." + field.getName()));
        assertSame(instance.get(ActionWithPointersOnMembers.class.getName() + "." + field.getName()), expected);

        /**
         * Initialize and test parameter with name feature.
         */
        instance.clear();
        ActionWithNamedMembers action3 = new ActionWithNamedMembers();
        action3.setTest("Hello name");
        field = ActionWithNamedMembers.class.getDeclaredField("test");
        field.setAccessible(true);
        result = instance.put(action3, field, null);
        assertNull(result);
        assertFalse(instance.isEmpty());
        assertEquals(1, instance.size());
        assertTrue(instance.contains("testingName"));
        assertSame(action3.getTest(), instance.get("testingName"));

        /**
         * Initialize and test RunContext pointer functionality.
         */
        instance.clear();
        ActionWithPointerToContext action4 = new ActionWithPointerToContext();
        action4.setDummy("dummy context");
        field = ActionWithPointerToContext.class.getDeclaredField("dummy");
        field.setAccessible(true);
        result = instance.put(action4, field, instance);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test storing RunContext field in parametr spool with
         * defined pointer.
         */
        instance.put(ptrKey, ptrValue);
        result = instance.put(action4, field, instance);
        assertNull(result);
        assertFalse(instance.isEmpty());
        assertEquals(2, instance.size());
        assertTrue(instance.contains(ptrValue));
        assertSame(action4.getDummy(), instance.get(ptrValue));
    }

    /**
     * Test of contains method, of class AbstractSpoolImpl.
     */
    @Test
    public void testContains() {
        /**
         * Initialize testing instance and variables for first test(protection
         * agains null parametr value).
         */
        ParameterSpool<Object, Object> instance = new ParameterSpoolImpl<>();
        String strKey = null;
        assertTrue(instance.isEmpty());
        boolean result = instance.contains(strKey);
        assertFalse(result);

        /**
         * Test functionality with searching key by string value.
         */
        strKey = "var1";
        SimpleParameterKey key = new SimpleParameterKey(strKey);
        Integer value = null;
        Object put = instance.put(key, value);
        assertNull(put);
        assertFalse(instance.isEmpty());
        result = instance.contains(strKey);
        assertTrue(result);
        assertSame(value, instance.get(strKey));

        /**
         * Test functionality with wrong implementation of custom anonymus key.
         * Key must overwrite equals and hash method to generate hash an compare
         * value based on string representation. In this case default equal
         * doesnt work correctely.
         */
        instance.clear();
        ParameterKey<BigInteger> internalKey = new ParameterKey<BigInteger>() {
            protected BigInteger keyValue;

            @Override
            public boolean fromString(String key) {
                if ((key == null) || (key.isEmpty())) {
                    return false;
                }
                keyValue = new BigInteger(key);
                return true;
            }

            @Override
            public boolean set(BigInteger key) {
                if (key == null) {
                    return false;
                }
                this.keyValue = key;
                return true;
            }

            @Override
            public BigInteger get() {
                return keyValue;
            }

            @Override
            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        };

        internalKey.set(BigInteger.ONE);
        strKey = "1";
        value = 1;
        put = instance.put(internalKey, value);
        assertNull(put);
        assertFalse(instance.isEmpty());
        result = instance.contains(strKey);
        assertFalse(result);

        /**
         * Test functionality with valid custom key class. Key class is based on
         * interface and implements correctely equals and hashcode methods for
         * comparing with other types of key.
         */
        instance.clear();
        internalKey = new ParameterKey<BigInteger>() {
            protected BigInteger keyValue;

            @Override
            public boolean fromString(String key) {
                if ((key == null) || (key.isEmpty())) {
                    return false;
                }
                keyValue = new BigInteger(key);
                return true;
            }

            @Override
            public boolean set(BigInteger key) {
                if (key == null) {
                    return false;
                }
                this.keyValue = key;
                return true;
            }

            @Override
            public BigInteger get() {
                return keyValue;
            }

            @Override
            public Object clone() throws CloneNotSupportedException {
                return super.clone();
            }

            @Override
            public int hashCode() {
                return (this.keyValue != null ? this.keyValue.toString().hashCode() : 0);
            }

            @Override
            public String toString() {
                return keyValue.toString();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (!(obj instanceof AbstractSpoolKey)) {
                    return false;
                }
                final AbstractSpoolKey<?> other = (AbstractSpoolKey<?>) obj;

                if (this.keyValue != other.get() && (this.keyValue == null || !this.keyValue.toString().equals(other.get().toString()))) {
                    return false;
                }
                return true;
            }
        };

        internalKey.set(BigInteger.ONE);
        put = instance.put(internalKey, value);
        assertNull(put);
        assertFalse(instance.isEmpty());

        result = instance.contains(strKey);
        assertTrue(result);
        assertSame(value, instance.get(strKey));

        internalKey = new CustomKey(BigInteger.ONE);
        put = instance.put(internalKey, value);
        assertNotNull(put);
        assertSame(value, put);
        assertFalse(instance.isEmpty());

        result = instance.contains(strKey);
        assertTrue(result);
        assertSame(value, instance.get(strKey));
    }

    /**
     * Test of get method, of class AbstractSpoolImpl.
     */
    @Test
    public void testGet_String() {
        /**
         * Initialize testing instance and variables for first test(protection
         * agains null parametr value).
         */
        ParameterSpool<Object, Object> instance = new ParameterSpoolImpl<>();
        String strKey = null;
        assertTrue(instance.isEmpty());
        Object result = instance.get(strKey);
        assertNull(result);

        /**
         * Initialize testing data to test instance method get with string
         * parameter.
         */
        Map<SimpleParameterKey, String> valid = new HashMap<>();
        Random r = new Random();
        int amountzofData = 10;
        for (int i = 0; i < amountzofData; i++) {
            byte[] by = new byte[r.nextInt(10) + 1];
            r.nextBytes(by);
            SimpleParameterKey key = new SimpleParameterKey(new String(by));
            by = new byte[r.nextInt(20) + 1];
            String value = new String(by);
            valid.put(key, value);
            instance.put(key, value);
        }

        /**
         * Test if all data in valid map are in tested instance.
         */
        for (SimpleParameterKey k : valid.keySet()) {
            assertSame(valid.get(k), instance.get(k.get()));
        }

        /**
         * Test data by randomly generated texts.
         */
        SimpleParameterKey tmp = new SimpleParameterKey(null);
        for (int i = 0; i < 500; i++) {
            byte[] by = new byte[r.nextInt(50)];
            r.nextBytes(by);
            strKey = new String(by);
            tmp.set(strKey);
            result = instance.get(strKey);

            if (valid.containsKey(tmp)) {
                assertSame(valid.get(tmp), result);
            } else {
                assertNull(result);
            }
        }

    }

    /**
     * Test of get method, of class AbstractSpoolImpl.
     */
    @Test
    public void testGet_3args() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        /**
         * Initialize testing instance and variables for first test(protection
         * agains null parameters value).
         */
        ParameterSpool<Object, Object> instance = new ParameterSpoolImpl<>();
        assertTrue(instance.isEmpty());

        ActionWithMembers action0 = null;
        Field field = null;
        Object result = instance.get(action0, field, null);
        assertNull(result);

        action0 = new ActionWithMembers();
        result = instance.get(action0, field, null);
        assertNull(result);
        result = instance.get(action0, field, instance);
        assertNull(result);

        field = ActionWithMembers.class.getDeclaredField("text");
        field.setAccessible(true);
        result = instance.get(null, field, null);
        assertNull(result);

        result = instance.get(null, field, instance);
        assertNull(result);

        /**
         * Testing get first element by valid values, but from empty spool.
         */
        result = instance.get(action0, field, null);
        assertNull(result);
        result = instance.get(action0, field, instance);
        assertNull(result);

        /**
         * Initialize instance by first element and test get method.
         */
        String testingStrValue = "Testing value";
        action0.setText(testingStrValue);
        instance.put(action0, field, null);
        assertFalse(instance.isEmpty());

        String newStrValue = "Hello im new one";
        action0.setText(newStrValue);
        result = instance.get(action0, field, instance);
        assertSame(newStrValue, result);
        assertSame(testingStrValue, action0.getText());

        /**
         * Test injection of value with key based directely on
         * SimpleParameterKey.
         */
        instance.clear();
        SimpleParameterKey key = new SimpleParameterKey(ActionWithMembers.class.getName() + "." + field.getName());
        instance.put(key, newStrValue);
        assertFalse(instance.isEmpty());
        result = instance.get(action0, field, instance);
        assertSame(newStrValue, action0.getText());
        assertSame(testingStrValue, result);

        /**
         * Test injection of value with key defined by spool from string value.
         */
        instance.clear();
        instance.put(ActionWithMembers.class.getName() + "." + field.getName(), testingStrValue);
        result = instance.get(action0, field, instance);
        assertSame(newStrValue, result);
        assertSame(testingStrValue, action0.getText());

        /**
         * Test parameters pointer functionality.
         */
        instance.clear();
        ActionWithPointersOnMembers action1 = new ActionWithPointersOnMembers();
        field = ActionWithPointersOnMembers.class.getDeclaredField("test");
        field.setAccessible(true);

        /**
         * Initialize and test data needed by pointer feature in parameter
         * spool. Both key's are instance of CustomKey type.
         */
        CustomKey ptrKey = new CustomKey(BigInteger.ONE);
        CustomKey ptrValue = new CustomKey(BigInteger.TEN);

        instance.put(ptrKey, ptrValue);
        instance.put(ptrValue, testingStrValue);
        assertFalse(instance.isEmpty());
        assertEquals(2, instance.size());

        result = instance.get(action1, field, instance);
        assertNull(result);
        assertSame(testingStrValue, action1.getTest());

        /**
         * Test get with defined pointer without defined key in spool for
         * pointers.
         */
        instance.remove(ptrKey);
        action1.setTest(newStrValue);
        result = instance.get(action1, field, instance);
        assertNull(result);
        assertSame(newStrValue, action1.getTest());

        /**
         * Test pointer feature with pointer key generated by instance.
         */
        instance.put("1", ptrValue);
        result = instance.get(action1, field, instance);
        assertSame(newStrValue, result);
        assertSame(testingStrValue, action1.getTest());

        /**
         * Test pointer feature with all keys needed by pointer functionality
         * generated by spool.
         */
        instance.put("10", newStrValue);
        result = instance.get(action1, field, instance);
        assertSame(testingStrValue, result);
        assertSame(newStrValue, action1.getTest());

        /**
         * Test getting non pointer parameter value in case when pointer key is
         * not exists.
         */
        instance.remove(ptrKey);
        String nonPointerValue = "Hello non pointers";
        instance.put(ActionWithPointersOnMembers.class.getName() + "." + field.getName(), nonPointerValue);
        result = instance.get(action1, field, instance);
        assertSame(newStrValue, result);
        assertSame(nonPointerValue, action1.getTest());

        /**
         * Test getting pointer value in cas when non pointer value is stored in
         * math togather with pointer data.
         */
        instance.put(ptrKey, ptrValue);
        result = instance.get(action1, field, instance);
        assertSame(nonPointerValue, result);
        assertSame(newStrValue, action1.getTest());

        /**
         * Test protection to get directly context from parameter spool without
         * using pointer feature.
         */
        instance.clear();
        ActionWithPointerToContext action2 = new ActionWithPointerToContext();
        field = ActionWithPointerToContext.class.getDeclaredField("dummy");
        field.setAccessible(true);
        action2.setDummy(nonPointerValue);
        instance.put(ActionWithPointerToContext.class.getName() + "." + field.getName(), null);

        result = instance.get(action2, field, instance);
        assertNull(result);
        assertSame(nonPointerValue, action2.getDummy());

        /**
         * Test valid context pointer injection from defined value by pointer in
         * parameter spool.
         */
        instance.put(ptrKey, ptrValue);
        instance.put(ptrValue, newStrValue);

        result = instance.get(action2, field, instance);
        assertSame(nonPointerValue, result);
        assertSame(newStrValue, action2.getDummy());
    }

    /**
     * Test of clone method, of class AbstractSpoolImpl.
     */
    @Test
    public void testClone() {
        /**
         * Initialize testing instance and test if clone create different
         * instance of spool.
         */
        ParameterSpool<Object, Object> instance = new ParameterSpoolImpl<>();
        assertTrue(instance.isEmpty());
        ParameterSpool<Object, Object> clone = (ParameterSpool<Object, Object>) instance.clone();
        assertNotSame(instance, clone);

        /**
         * Test cloning values of primitive type and spool data separation.
         */
        SimpleParameterKey key = new SimpleParameterKey("Hello");
        int value = 1;
        instance.put(key, value);

        clone = (ParameterSpool<Object, Object>) instance.clone();
        assertNotSame(instance, clone);
        int cloneValue = 2;
        clone.put(key, cloneValue);
        assertSame(value, instance.get(key));
        assertSame(cloneValue, clone.get(key));
        clone.clear();
        assertFalse(instance.isEmpty());
        assertTrue(clone.isEmpty());

        /**
         * Test cloning with non clonable values stored in spool. This type of
         * value is global for all cloned instances of spool.
         */
        instance.clear();
        ActionWithMembers nonClonableObject = new ActionWithMembers();
        String testText = "Origin";
        nonClonableObject.setText(testText);
        instance.put(key, nonClonableObject);
        assertFalse(instance.isEmpty());

        clone = (ParameterSpool<Object, Object>) instance.clone();
        assertNotSame(instance, clone);
        nonClonableObject = (ActionWithMembers) clone.get(key);

        assertSame(testText, ((ActionWithMembers) instance.get(key)).getText());
        assertSame(testText, nonClonableObject.getText());
        String newText = "Clone";
        nonClonableObject.setText(newText);
        assertSame(newText, ((ActionWithMembers) instance.get(key)).getText());
        assertSame(newText, ((ActionWithMembers) clone.get(key)).getText());

        /**
         * Testin cloning with clonable values stored in spool.
         */
        instance.clear();
        CloneableObject<String> cloneableValue = new CloneableObject<>(testText);
        instance.put(key, cloneableValue);
        assertFalse(instance.isEmpty());

        clone = (ParameterSpool<Object, Object>) instance.clone();
        assertNotSame(instance, clone);
        assertNotSame(cloneableValue, clone.get(key));
        assertSame(testText, ((CloneableObject<String>) instance.get(key)).getValue());
        assertEquals(testText, ((CloneableObject<String>) clone.get(key)).getValue());
        cloneableValue = (CloneableObject<String>) clone.get(key);
        cloneableValue.setValue(newText);
        assertSame(testText, ((CloneableObject<String>) instance.get(key)).getValue());
        assertSame(newText, ((CloneableObject<String>) clone.get(key)).getValue());
    }
}
