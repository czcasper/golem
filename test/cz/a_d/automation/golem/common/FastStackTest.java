/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
