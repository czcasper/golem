/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.enums;

import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.spools.actions.ActionInfoProxyImpl;
import com.ca.automation.golem.spools.actions.testClass.ActionWithMembers;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maslu02
 */
public class ActionMethodProxyTypeTest {

    public ActionMethodProxyTypeTest() {
    }

    /**
     * Test of getType method, of class ActionMethodProxyType.
     */
    @Test
    public void testGetType() {
        List<Class<? extends Annotation>> annots = new ArrayList<Class<? extends Annotation>>();
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            Class<? extends Annotation> result = t.getAnnotation();
            assertNotNull(result);
            annots.add(result);
        }

        for(Class<? extends Annotation> c : annots){
            ActionMethodProxyType type = ActionMethodProxyType.getType(c);
            assertNotNull(type);
        }
    }

    /**
     * Test of isCritical method, of class ActionMethodProxyType.
     */
    @Test
    public void testIsCritical() {
        Annotation annotation = null;
        ActionMethodProxyType instance = ActionMethodProxyType.Initialize;
        boolean result = instance.isCritical(annotation);
        assertFalse(result);
        
        
        ActionInfoProxy tmpProxy= new ActionInfoProxyImpl(ActionInfoProxyImpl.createNewComparators());
        boolean loadAction = tmpProxy.loadAction(ActionWithMembers.class, null);
        assertTrue(loadAction);
        
        for(ActionMethodProxyType t : ActionMethodProxyType.values()){
            SortedSet<Method> method = tmpProxy.getMethod(t);
            for(Method m : method){
                annotation = m.getAnnotation(t.getAnnotation());
                assertTrue(t.isCritical(annotation));
            }
        }
    }

    /**
     * Test of getAnnotation method, of class ActionMethodProxyType.
     */
    @Test
    public void testGetAnnotation() {
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            Class<? extends Annotation> result = t.getAnnotation();
            assertNotNull(result);
        }
    }
}
