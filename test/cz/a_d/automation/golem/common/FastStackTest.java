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
package cz.a_d.automation.golem.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author casper
 */
public class FastStackTest {
    
    public FastStackTest() {
    }

    /**
     * Test of peek method, of class FastStack.
     */
    @Test
    public void testPeek() {
        FastStack<Integer> instance = new FastStack<>();
        Integer result = instance.peek();
        assertNull(result);
        
        for(int i=0;i<20;i++){
            instance.push(i);
        }
        assertEquals(instance.peek(), instance.getFirst());
    }

    /**
     * Test of push method, of class FastStack.
     */
    @Test
    public void testPush() {
        FastStack<Integer> instance = new FastStack<>();
        for(int i=0;i<20;i++){
            instance.push(i);
        }
        assertEquals(instance.size(), 20);
    }

    /**
     * Test of pop method, of class FastStack.
     */
    @Test
    public void testPop() {
        FastStack<Integer> instance = new FastStack<>();
        List<Integer> data = new ArrayList<>();
        for(int i=0;i<50;i++){
            data.add(i);
            instance.push(i);
        }
        Collections.sort(data, Collections.reverseOrder());
        int index=0;
        while(!instance.isEmpty()){
            assertEquals(data.get(index++), instance.pop());
        }
    }
}
