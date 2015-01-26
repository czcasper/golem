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
package cz.a_d.automation.golem.spools.enums;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author casper
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
