/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.spools.ActionInformationSpool;
import cz.a_d.automation.golem.spools.actions.ActionInfoProxyImpl;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author maslu02
 */
public class ActionInformationSpoolImplTest {
    
    public ActionInformationSpoolImplTest() {
    }

    /**
     * Test of getGlobal method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGetGlobal() {
        /**
         * Declare and initialize variables for testing functionality of static method getGlobal.
         */
        ActionInformationSpool<Object> result = ActionInformationSpoolImpl.getGlobal();
        assertNotNull(result);
        
        ActionInfoProxy testProxy = new ActionInfoProxyImpl(ActionInfoProxyImpl.createNewComparators());
        testProxy.loadAction(ActionWithMembers.class, result);
        
        /**
         * Test simple common use case scenarion for global spool.
         */
        result.clear();
        assertTrue(result.isEmpty());
        result.put("test", testProxy);
        assertFalse(result.isEmpty());
        result = null;
        assertNull(result);
        result = ActionInformationSpoolImpl.getGlobal();
        assertFalse(result.isEmpty());
        ActionInfoProxy get = result.get("test");
        assertSame(testProxy, get);
        result.clear();
    }

    /**
     * Test of newInstance method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testNewInstance() {
        System.out.println("newInstance");
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        ActionInformationSpool expResult = null;
        ActionInformationSpool result = instance.newInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidAction method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testIsValidAction_Object() {
        System.out.println("isValidAction");
        Object action = null;
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        boolean expResult = false;
        boolean result = instance.isValidAction(action);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidAction method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testIsValidAction_Class() {
        System.out.println("isValidAction");
        Class<?> action = null;
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        boolean expResult = false;
        boolean result = instance.isValidAction(action);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createNewFromObject method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testCreateNewFromObject() {
        System.out.println("createNewFromObject");
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        ActionStream expResult = null;
        ActionStream result = instance.createNewFromObject(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createNewFromClasses method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testCreateNewFromClasses() {
        System.out.println("createNewFromClasses");
        List<Class<?>> actions = null;
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        ActionStream expResult = null;
        ActionStream result = instance.createNewFromClasses(actions);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of containsKey method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testContainsKey() {
        System.out.println("containsKey");
        Object key = null;
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        boolean expResult = false;
        boolean result = instance.containsKey(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGet_String() {
        System.out.println("get");
        String key = "";
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        ActionInfoProxy expResult = null;
        ActionInfoProxy result = instance.get(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGet_Object() {
        System.out.println("get");
//        Object key = null;
//        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
//        ActionInfoProxy expResult = null;
//        ActionInfoProxy result = instance.get(key);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of put method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testPut() throws Exception {
        System.out.println("put");
//        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
//        ActionInfoProxy expResult = null;
//        ActionInfoProxy result = instance.put(null,null,null);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGet_3args() throws Exception {
        System.out.println("get");
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        ActionInfoProxy expResult = null;
        ActionInfoProxy result = instance.get(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
