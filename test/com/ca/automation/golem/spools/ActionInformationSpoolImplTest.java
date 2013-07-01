/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools;

import com.ca.automation.golem.interfaces.ActionStream;
import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.interfaces.spools.ActionInformationSpool;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

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
        System.out.println("getGlobal");
        ActionInformationSpool expResult = null;
        ActionInformationSpool result = ActionInformationSpoolImpl.getGlobal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        Object key = null;
        ActionInformationSpoolImpl instance = new ActionInformationSpoolImpl();
        ActionInfoProxy expResult = null;
        ActionInfoProxy result = instance.get(key);
        assertEquals(expResult, result);
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
