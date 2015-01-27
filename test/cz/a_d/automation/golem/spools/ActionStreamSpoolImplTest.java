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
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.ActionInformationSpool;
import cz.a_d.automation.golem.interfaces.spools.ActionStreamSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;
import cz.a_d.automation.golem.spools.keys.SimpleActionStreamKey;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionForTestingContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithClone;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithConnection;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithNamedMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithPointerToContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithPointersOnMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithRetValues;
import cz.a_d.automation.testClasses.actions.dummy.valid.SimpleValidAction;
import cz.a_d.automation.testClasses.actions.dummy.valid.ValidActionWithNoRun;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author casper
 */
public class ActionStreamSpoolImplTest {

    protected List<Class<?>> classStream = new ArrayList<>();
    protected List<Class<?>> clonableStream = new ArrayList<>();
    protected ActionInformationSpool<Object> infoSpool = ActionInformationSpoolImpl.getGlobal();
    @Rule
    public ExpectedException testException = ExpectedException.none();

    public ActionStreamSpoolImplTest() {
        classStream.add(ActionForTestingContext.class);
        classStream.add(ActionWithConnection.class);
        classStream.add(ActionWithContext.class);
        classStream.add(ActionWithMembers.class);
        classStream.add(ActionWithNamedMembers.class);
        classStream.add(ActionWithPointerToContext.class);
        classStream.add(ActionWithPointersOnMembers.class);
        classStream.add(ActionWithRetValues.class);
        classStream.add(SimpleValidAction.class);
        classStream.add(ValidActionWithNoRun.class);

        clonableStream.add(ActionWithClone.class);
    }

    /**
     * Test of getGlobal method, of class ActionStreamSpoolImpl.
     */
    @Test
    public void testGetGlobal() {
        /**
         * Initialize and test one common usecase scenarion for globally defined
         * spool.
         */
        ActionStreamSpool<Object, Object> instance = ActionStreamSpoolImpl.getGlobal();
        assertNotNull(instance);
        ActionStream<Object, Object> value = infoSpool.createNewFromClasses(classStream);
        instance.putFrom("test", value);
        assertFalse(instance.isEmpty());
        instance = null;
        assertNull(instance);

        instance = ActionStreamSpoolImpl.getGlobal();
        ActionStream<Object, Object> result = instance.getFrom("test");
        assertNotNull(result);
        assertNotSame(value, result);
    }

    /**
     * Test of newInstance method, of class ActionStreamSpoolImpl.
     */
    @Test
    public void testNewInstance() {
        /**
         * Initialize instance for testing and add data inside.
         */
        ActionStreamSpool<Object, Object> instance = new ActionStreamSpoolImpl<>();
        assertTrue(instance.isEmpty());
        ActionStream<Object, Object> value = infoSpool.createNewFromClasses(classStream);
        instance.putFrom("test", value);
        assertFalse(instance.isEmpty());

        /**
         * Test creation of new clean instance from existing instance.
         */
        ActionStreamSpool<Object, Object> result = instance.newInstance();
        assertTrue(result.isEmpty());
        assertFalse(instance.isEmpty());

        /**
         * Test creation of second new clean instance from existing one.
         */
        ActionStreamSpool<Object, Object> result2 = instance.newInstance();
        assertNotSame(instance, result);
        assertNotSame(instance, result2);
        assertNotSame(result, result2);
        assertFalse(instance.isEmpty());
        assertTrue(result.isEmpty());
        assertTrue(result2.isEmpty());
    }

    @Test
    public void testPutFrom() {
        /**
         * Initialize and test protection of method agains null and empty
         * parameter values.
         */
        ActionStreamSpool<Object, Object> instance = new ActionStreamSpoolImpl<>();
        assertTrue(instance.isEmpty());
        String key = null;
        ActionStream<Object, Object> value = null;
        ActionStream<Object, Object> result = instance.putFrom(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());
        key = "";
        result = instance.putFrom(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());
        value = infoSpool.createNewFromClasses(classStream);
        result = instance.putFrom(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());
        key="valid key";
        value.clear();
        result = instance.putFrom(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());
        
        /**
         * Initialize and test spool by random keys with same values.
         */
        Random r = new Random();
        Set<String> testKeys = new HashSet<>();
        int count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            byte[] tmp = new byte[r.nextInt(60) + 20];
            testKeys.add(new String(tmp));
        }

        int expectedSize = instance.size();
        value = infoSpool.createNewFromClasses(classStream);
        for (String s : testKeys) {
            result = instance.putFrom(s, value);
            assertNull(result);
            assertEquals(++expectedSize, instance.size());
        }
    }

    @Test
    public void testGetFrom() {
        /**
         * Initialize and test protection of method agains null, empty and
         * invalid parameter values.
         */
        ActionStreamSpool<Object, Object> instance = new ActionStreamSpoolImpl<>();
        String key = null;
        ActionStream<Object, Object> result = instance.getFrom(key);
        assertNull(result);
        key = "";
        result = instance.getFrom(key);
        assertNull(result);

        /**
         * Initialize and test method by using randly generated strings for
         * putFrom and getFrom combinantion.
         */
        Random r = new Random();
        byte[] tmp;
        int testCount = r.nextInt(100) + 20;
        ActionStream<Object, Object> value = infoSpool.createNewFromClasses(classStream);

        for (int i = 0; i < testCount; i++) {
            tmp = new byte[r.nextInt(50) + 60];
            key = new String(tmp);
            instance.putFrom(key, value);
            result = instance.getFrom(key);
            assertNotNull(result);
            // TODO find better way for testing
//            assertArrayEquals(value.getActionList().toArray(), value.getActionList().toArray());
            assertNotSame(value, result);
        }
    }

    @Test
    public void testPut() {
        /**
         * Initialize and test instance method by null and invalid parameter
         * values.
         */
        ActionStreamSpool<Object, Object> instance = new ActionStreamSpoolImpl<>();
        assertTrue(instance.isEmpty());
        ActionStreamKey<String> key = null;
        ActionStream<Object, Object> value = null;
        ActionStream<Object, Object> result = instance.put(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());
        value = infoSpool.createNewFromClasses(classStream);
        result = instance.put(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());
        key = new SimpleActionStreamKey("test");
        // TODO considere if it is necessary
        value.clear();
        result = instance.put(key, value);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test spool by random unique keys.
         */
        Random r = new Random();
        Set<String> testKeys = new HashSet<>();
        int count = r.nextInt(50) + 50;
        for (int i = 0; i < count; i++) {
            byte[] tmp = new byte[r.nextInt(60) + 20];
            testKeys.add(new String(tmp));
        }

        int expectedSize = instance.size();
        value = infoSpool.createNewFromClasses(classStream);
        for (String s : testKeys) {
            key = new SimpleActionStreamKey(s);
            result = instance.put(key, value);
            assertNull(result);
            assertEquals(++expectedSize, instance.size());
        }
    }

    /**
     * Test of get method, of class ActionStreamSpoolImpl.
     */
    @Test
    public void testGet() {
        /**
         * Initialize and test instance method by null and invalid key.
         */
        ActionStreamSpool<Object, Object> instance = new ActionStreamSpoolImpl<>();
        ActionStreamKey<String> key = null;
        ActionStream<Object, Object> result = instance.get(key);
        assertNull(result);
        assertTrue(instance.isEmpty());
        key = new SimpleActionStreamKey(null);
        result = instance.get(key);
        assertNull(result);
        assertTrue(instance.isEmpty());

        /**
         * Initialize and test getting new instance of ActionStream from spool
         * with use case not clonable actions in stream.
         */
        ActionStream<Object, Object> nonClonable = infoSpool.createNewFromClasses(classStream);
        key.set("testKey");
        ActionStream<Object, Object> put = instance.put(key, nonClonable);
        assertNull(put);
        assertFalse(instance.isEmpty());
        result = instance.get(key);
        assertNotSame(nonClonable, result);
        Iterator<Object> baseIt = nonClonable.resetableIterator();
        Iterator<Object> resultIt = result.resetableIterator();
        while (resultIt.hasNext()) {
            assertSame(baseIt.next(), resultIt.next());
        }
        assertFalse(baseIt.hasNext());

        /**
         * Initialize and test getting new instance of ActionStream from spool
         * with clonable actions in stream.
         */
        ActionStream<Object, Object> clonable = infoSpool.createNewFromClasses(clonableStream);
        instance.clear();
        put = instance.put(key, clonable);
        assertNull(put);
        assertFalse(instance.isEmpty());
        result = instance.get(key);
        assertNotSame(clonable, result);
        baseIt = clonable.resetableIterator();
        resultIt = result.resetableIterator();
        while (resultIt.hasNext()) {
            assertNotSame(baseIt.next(), resultIt.next());
        }
        assertFalse(baseIt.hasNext());
    }

    /**
     * Test of testGet_3args method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGet_3args() throws Exception {
        /**
         * Initialize variables for testing exception in this method.
         */
        ActionStreamSpool<Object, Object> instance = new ActionStreamSpoolImpl<>();
        Object action = new ActionWithMembers();
        Field field = ActionWithMembers.class.getDeclaredField("text");
        testException.expect(IllegalAccessException.class);
        testException.expectMessage("Action stream spool doesn't support direct action interaction with action fields");
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
        ActionStreamSpool<Object, Object> instance = new ActionStreamSpoolImpl<>();
        testException.expect(IllegalAccessException.class);
        testException.expectMessage("Action stream spool doesn't support direct action interaction with action fields");
        instance.put(null, null, null);
    }
}
