/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.enums;

import java.lang.annotation.Annotation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maslu02
 */
public class ActionFieldProxyTypeTest {
    
    public ActionFieldProxyTypeTest() {
    }

    /**
     * Test of getType method, of class ActionFieldProxyType.
     */
    @Test
    public void testGetType() {
        Class<? extends Annotation> annotation = null;
        ActionFieldProxyType expResult = null;
        ActionFieldProxyType result = ActionFieldProxyType.getType(annotation);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAnnotation method, of class ActionFieldProxyType.
     */
    @Test
    public void testGetAnnotation() {
        System.out.println("getAnnotation");
        ActionFieldProxyType instance = null;
        Class expResult = null;
        Class result = instance.getAnnotation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
