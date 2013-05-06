/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author maslu02
 */
public class FastStackTest {
    
    public FastStackTest() {
    }

    /**
     * Test of peek method, of class FastStack.
     */
    @Test
    public void testPeek() {
        FastStack<Integer> instance = new FastStack<Integer>();
        Integer expResult = null;
        Integer result = instance.peek();
        assertEquals(expResult, result);
        
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
        FastStack<Integer> instance = new FastStack<Integer>();
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
        FastStack<Integer> instance = new FastStack<Integer>();
        List<Integer> data = new ArrayList<Integer>();
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
