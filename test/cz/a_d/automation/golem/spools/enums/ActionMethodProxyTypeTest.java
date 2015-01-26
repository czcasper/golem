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

import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.spools.actions.ActionInfoProxyImpl;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author casper
 */
public class ActionMethodProxyTypeTest {

    public ActionMethodProxyTypeTest() {
    }

    /**
     * Test of getType method, of class ActionMethodProxyType.
     */
    @Test
    public void testGetType() {
        List<Class<? extends Annotation>> annots = new ArrayList<>();
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
