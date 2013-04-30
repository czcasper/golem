/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.actions;

import com.ca.automation.golem.spools.actions.testClass.ActionWithConnection;
import com.ca.automation.golem.spools.actions.testClass.ActionWithContext;
import com.ca.automation.golem.spools.actions.testClass.ActionWithMembers;
import com.ca.automation.golem.spools.actions.testClass.ActionWithRetValues;
import com.ca.automation.golem.spools.actions.testClass.SimpleValidAction;
import com.ca.automation.golem.spools.actions.testClass.ValidActionWithNoRun;
import com.ca.automation.golem.spools.actions.testClass.WrongActionNoAnnots;
import com.ca.automation.golem.spools.actions.testClass.WrongNoRunMethod;
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
     * Test of getDefaultInstance method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetDefaultInstanceJDK() throws Exception {
        ActionReflectionContainer result = ActionReflectionContainer.getDefaultInstance();
        assertNotNull(result);
        ActionReflectionContainer result2 = ActionReflectionContainer.getDefaultInstance();
        assertSame(result, result2);
    }

    /**
     * Test of getFields method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetFields() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        WrongActionNoAnnots actionWr= new WrongActionNoAnnots();
        List result = instance.getFields(actionWr);
        assertNull(result);
        
        WrongNoRunMethod actionNoR= new WrongNoRunMethod();
        result = instance.getFields(actionNoR);
        assertNull(result);

        ActionWithMembers actionMem = new ActionWithMembers();
        
        result = instance.getFields(actionMem);
        assertNotNull(result);
        assertEquals(result.size(), 2);
    }

    /**
     * Test of getFieldByName method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetFieldByName_Object_String() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        ActionWithMembers action = new ActionWithMembers();
        Field result = instance.getFieldByName(action, "");
        assertNull(result);
        
        result = instance.getFieldByName(action, "text");
        assertNotNull(result);
        
        String text = "Hell share data";
        result.set(action, text);
        assertEquals(action.getText(), text);
        
        result = instance.getFieldByName(action, "upperCase");
        assertNotNull(result);
        
        boolean upper = true;
        assertFalse(action.isUpperCase());
        result.set(action, upper);
        assertTrue(action.isUpperCase());    
        
        ValidActionWithNoRun actWithParent = new ValidActionWithNoRun();
        result = instance.getFieldByName(actWithParent, "upperCase");
        assertNotNull(result);
        
        assertFalse(actWithParent.isUpperCase());
        result.set(actWithParent, true);
        assertTrue(actWithParent.isUpperCase());    
        
        result = instance.getFieldByName(actWithParent, "label");
        assertNotNull(result);
        
        text = "Hell share message:";
        result.set(actWithParent, text);
        assertEquals(actWithParent.getLabel(), text);
    }

    /**
     * Test of getConnections method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetConnections() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        ActionWithConnection action = new ActionWithConnection();
        List<Field> result = instance.getConnections(new ValidActionWithNoRun());
        assertTrue(result.isEmpty());
        
        result = instance.getConnections(action);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    /**
     * Test of getRetValues method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetRetValues() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        ActionWithRetValues action = new ActionWithRetValues();
        List<Field> result = instance.getRetValues(new ValidActionWithNoRun());
        assertTrue(result.isEmpty());
        
        result = instance.getRetValues(action);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    /**
     * Test of getContexts method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetContexts() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        ActionWithContext action = new ActionWithContext();
        List<Field> result = instance.getContexts(new ValidActionWithNoRun());
        assertTrue(result.isEmpty());
        
        result = instance.getContexts(action);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    /**
     * Test of getInits method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetInits() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        ActionWithContext action = new ActionWithContext();
        List<Method> result = instance.getInits(new SimpleValidAction());
        assertTrue(result.isEmpty());
        
        result = instance.getInits(action);
        assertNotNull(result);
        assertEquals(result.size(), 2);
        
        //TOD test ordering of methods by order parameter
    }

    /**
     * Test of getRuns method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetRuns() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        ActionWithContext action = new ActionWithContext();
        List<Method> result = instance.getRuns(new SimpleValidAction());
        assertEquals(result.size(),1);
        
        result = instance.getRuns(action);
        assertNotNull(result);
        assertEquals(result.size(), 2);
    }

    /**
     * Test of getValidates method, of class ActionReflectionContainer.
     */
    @Test
    public void testGetValidates() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        ActionWithContext action = new ActionWithContext();
        List<Method> result = instance.getValidates(new SimpleValidAction());
        assertTrue(result.isEmpty());
        
        result = instance.getValidates(action);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    /**
     * Test of isAction method, of class ActionReflectionContainer.
     */
    @Test
    public void testIsAction() throws Exception {
        ActionReflectionContainer instance = ActionReflectionContainer.getDefaultInstance();
        WrongActionNoAnnots actionWr= new WrongActionNoAnnots();
        boolean result = instance.isAction(actionWr);
        assertFalse(result);
        
        WrongNoRunMethod actionNoR= new WrongNoRunMethod();
        result = instance.isAction(actionNoR);
        assertFalse(result);

        ActionWithContext action = new ActionWithContext();
        result = instance.isAction(action);        
        assertTrue(result);        
    }
}
