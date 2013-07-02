/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools.enums;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

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
        List<Class<? extends Annotation>> annotations = new ArrayList<>();
        for(ActionFieldProxyType t : ActionFieldProxyType.values()){
            Class<? extends Annotation> annotation = t.getAnnotation();
            if(annotation!=null){
                annotations.add(annotation);
            }
        }
        assertFalse(annotations.isEmpty());
        
        for(Class<? extends Annotation> cl : annotations){
            assertNotNull(ActionFieldProxyType.getType(cl));
        }
        
    }

    /**
     * Test of getAnnotation method, of class ActionFieldProxyType.
     */
    @Test
    public void testGetAnnotation() {
        List<Class<? extends Annotation>> annotations = new ArrayList<>();
        for(ActionFieldProxyType t : ActionFieldProxyType.values()){
            Class<? extends Annotation> annotation = t.getAnnotation();
            if(annotation!=null){
                annotations.add(annotation);
            }
        }
        assertFalse((annotations.isEmpty()));        
    }
}
