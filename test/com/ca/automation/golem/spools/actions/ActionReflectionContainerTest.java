/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.actions;

import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.interfaces.spools.ActionInformationSpool;
import com.ca.automation.golem.spools.ActionInformationSpoolImpl;
import com.ca.automation.golem.spools.actions.testClass.ActionWithConnection;
import com.ca.automation.golem.spools.actions.testClass.ActionWithContext;
import com.ca.automation.golem.spools.actions.testClass.ActionWithMembers;
import com.ca.automation.golem.spools.actions.testClass.ActionWithRetValues;
import com.ca.automation.golem.spools.actions.testClass.SimpleValidAction;
import com.ca.automation.golem.spools.actions.testClass.ValidActionWithNoRun;
import com.ca.automation.golem.spools.actions.testClass.WrongActionNoAnnots;
import com.ca.automation.golem.spools.actions.testClass.WrongNoRunMethod;
import com.ca.automation.golem.spools.enums.ActionFieldProxyType;
import com.ca.automation.golem.spools.enums.ActionMethodProxyType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author maslu02
 */
public class ActionReflectionContainerTest {

//    protected static EJBContainer container=null;
    public ActionReflectionContainerTest() {
    }

    /**
     * Test of getDefaultInstance method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGetDefaultInstanceJDK() throws Exception {
        ActionInformationSpool<Object> result = ActionInformationSpoolImpl.getGlobal();
        assertNotNull(result);
        ActionInformationSpool<Object> result2 = ActionInformationSpoolImpl.getGlobal();
        assertSame(result, result2);
    }

    /**
     * Test of getFields method, of class ActionInformationSpoolImpl.
     */
    @Test
    public void testGetFields() throws Exception {
        ActionInformationSpool<Object> instance = ActionInformationSpoolImpl.getGlobal();
        WrongActionNoAnnots actionWr= new WrongActionNoAnnots();
        ActionInfoProxy result = instance.get(actionWr);
        assertNull(result);
        
        WrongNoRunMethod actionNoR= new WrongNoRunMethod();
        result = instance.get(actionNoR);
        assertNull(result);

        ActionWithMembers actionMem = new ActionWithMembers();
        
        result = instance.get(actionMem);
        assertNotNull(result);
        assertEquals(result.getField(ActionFieldProxyType.Parameters), 2);
        assertEquals(result.getFieldNames().size(), 2);
        assertEquals(result.getMethod(ActionMethodProxyType.Initialize), 1);
        assertEquals(result.getMethod(ActionMethodProxyType.Run), 1);
    }

    /**
     * Test of getFieldByName method, of class ActionInformationSpoolImpl.
     */
//    @Test
//    public void testGetFieldByName_Object_String() throws Exception {
//        ActionInformationSpool<Object> instance = ActionInformationSpoolImpl.getGlobal();
//        ActionWithMembers action = new ActionWithMembers();
//        ActionInfoProxy result = instance.get(action);
//        
//        result = instance.getFieldByName(action, "text");
//        assertNotNull(result);
//        
//        String text = "Hell share data";
//        result.set(action, text);
//        assertEquals(action.getText(), text);
//        
//        result = instance.getFieldByName(action, "upperCase");
//        assertNotNull(result);
//        
//        boolean upper = true;
//        assertFalse(action.isUpperCase());
//        result.set(action, upper);
//        assertTrue(action.isUpperCase());    
//        
//        ValidActionWithNoRun actWithParent = new ValidActionWithNoRun();
//        result = instance.getFieldByName(actWithParent, "upperCase");
//        assertNotNull(result);
//        
//        assertFalse(actWithParent.isUpperCase());
//        result.set(actWithParent, true);
//        assertTrue(actWithParent.isUpperCase());    
//        
//        result = instance.getFieldByName(actWithParent, "label");
//        assertNotNull(result);
//        
//        text = "Hell share message:";
//        result.set(actWithParent, text);
//        assertEquals(actWithParent.getLabel(), text);
//    }
//
//    /**
//     * Test of getConnections method, of class ActionInformationSpoolImpl.
//     */
//    @Test
//    public void testGetConnections() throws Exception {
//        ActionInformationSpoolImpl instance = ActionInformationSpoolImpl.getDefaultInstance();
//        ActionWithConnection action = new ActionWithConnection();
//        List<Field> result = instance.getConnections(new ValidActionWithNoRun());
//        assertTrue(result.isEmpty());
//        
//        result = instance.getConnections(action);
//        assertNotNull(result);
//        assertEquals(result.size(), 1);
//    }
//
//    /**
//     * Test of getRetValues method, of class ActionInformationSpoolImpl.
//     */
//    @Test
//    public void testGetRetValues() throws Exception {
//        ActionInformationSpoolImpl instance = ActionInformationSpoolImpl.getDefaultInstance();
//        ActionWithRetValues action = new ActionWithRetValues();
//        List<Field> result = instance.getRetValues(new ValidActionWithNoRun());
//        assertTrue(result.isEmpty());
//        
//        result = instance.getRetValues(action);
//        assertNotNull(result);
//        assertEquals(result.size(), 1);
//    }
//
//    /**
//     * Test of getContexts method, of class ActionInformationSpoolImpl.
//     */
//    @Test
//    public void testGetContexts() throws Exception {
//        ActionInformationSpoolImpl instance = ActionInformationSpoolImpl.getDefaultInstance();
//        ActionWithContext action = new ActionWithContext();
//        List<Field> result = instance.getContexts(new ValidActionWithNoRun());
//        assertTrue(result.isEmpty());
//        
//        result = instance.getContexts(action);
//        assertNotNull(result);
//        assertEquals(result.size(), 1);
//    }
//
//    /**
//     * Test of getInits method, of class ActionInformationSpoolImpl.
//     */
//    @Test
//    public void testGetInits() throws Exception {
//        ActionInformationSpoolImpl instance = ActionInformationSpoolImpl.getDefaultInstance();
//        ActionWithContext action = new ActionWithContext();
//        List<Method> result = instance.getInits(new SimpleValidAction());
//        assertTrue(result.isEmpty());
//        
//        result = instance.getInits(action);
//        assertNotNull(result);
//        assertEquals(result.size(), 2);
//        
//        //TOD test ordering of methods by order parameter
//    }
//
//    /**
//     * Test of getRuns method, of class ActionInformationSpoolImpl.
//     */
//    @Test
//    public void testGetRuns() throws Exception {
//        ActionInformationSpoolImpl instance = ActionInformationSpoolImpl.getDefaultInstance();
//        ActionWithContext action = new ActionWithContext();
//        List<Method> result = instance.getRuns(new SimpleValidAction());
//        assertEquals(result.size(),1);
//        
//        result = instance.getRuns(action);
//        assertNotNull(result);
//        assertEquals(result.size(), 2);
//    }
//
//    /**
//     * Test of getValidates method, of class ActionInformationSpoolImpl.
//     */
//    @Test
//    public void testGetValidates() throws Exception {
//        ActionInformationSpoolImpl instance = ActionInformationSpoolImpl.getDefaultInstance();
//        ActionWithContext action = new ActionWithContext();
//        List<Method> result = instance.getValidates(new SimpleValidAction());
//        assertTrue(result.isEmpty());
//        
//        result = instance.getValidates(action);
//        assertNotNull(result);
//        assertEquals(result.size(), 1);
//    }
//
//    /**
//     * Test of isAction method, of class ActionInformationSpoolImpl.
//     */
//    @Test
//    public void testIsAction() throws Exception {
//        ActionInformationSpoolImpl instance = ActionInformationSpoolImpl.getDefaultInstance();
//        WrongActionNoAnnots actionWr= new WrongActionNoAnnots();
//        boolean result = instance.isAction(actionWr);
//        assertFalse(result);
//        
//        WrongNoRunMethod actionNoR= new WrongNoRunMethod();
//        result = instance.isAction(actionNoR);
//        assertFalse(result);
//
//        ActionWithContext action = new ActionWithContext();
//        result = instance.isAction(action);        
//        assertTrue(result);        
//    }
}
