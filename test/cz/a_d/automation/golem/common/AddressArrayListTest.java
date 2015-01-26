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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author krera03
 */
public class AddressArrayListTest {
    /* define objects for this test */
    private ArrayList<Integer> arr1, arr2;
    private Map<String, Integer> dict;
    private Object o;
    
    public AddressArrayListTest() {
        arr1 = new ArrayList<>();
        arr2 = new ArrayList<>();
        for(int i=0;i<=10;i++){
            Integer j = new Integer(i);
            arr1.add(j);
            arr2.add(j);
        }   
        o = null;
        dict = new HashMap<>();
        dict.put("one", 1);
        dict.put("two", 2);        
    }
        
    @Before
    public void setUp() {
       /* Define content for test objects */ 
       // ArrayList arr1 = new ArrayList();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of indexOf method, of class AddressArrayList.
     */
    @Test
    public void testIndexOf() {
        
        Logger.getLogger(this.getClass().getName()).fine("indexOf");
       
        AddressArrayList<Object> instance = new AddressArrayList<>();
        
        instance.add(o);
        instance.add(arr1);
        instance.add(dict);
        
        int expResult = 0;
        int result = instance.indexOf(o);
        assertEquals(expResult, result);

    }
    
    /** 
     * Test of contains method, which is using method indexOf
     */
    @Test
    public void testContains(){
        Logger.getLogger(this.getClass().getName()).fine("contains");
        AddressArrayList<Object> instance = new AddressArrayList<>();
        
        instance.add(o);
        instance.add(arr1);
        instance.add(dict);

        assertTrue(instance.contains(o));
        assertTrue(instance.contains(arr1));
        assertTrue(instance.contains(dict));    
    }
    
    
    
    /**
     * Test of lastIndexOf method, of class AddressArrayList.
     */
    @Test
    public void testLastIndexOf() {
        Logger.getLogger(this.getClass().getName()).fine("lastIndexOf");
        //Object o = null;
        AddressArrayList<Object> instance = new AddressArrayList<>();
        
        instance.add(o);
        instance.add(arr1);
        instance.add(dict);
        
        int expResult = 2;
        int result = instance.lastIndexOf(dict);
        assertEquals(expResult, result);
    }
    
    /**
     * Test whether two identical objects are stored as two different items *
     */
    @Test
    public void testIdenticalItems() {
        Logger.getLogger(this.getClass().getName()).fine("identical objects");
        AddressArrayList<Object> instance = new AddressArrayList<>();
     /*   ArrayList arr1 = new ArrayList();
        ArrayList arr2 = new ArrayList();
        for(int i=1;i<=10;i++){
            arr1.add(i);
            arr2.add(i);
        }         */
        
        Iterator<Integer> it1 = arr1.iterator();
        Iterator<Integer> it2 = arr2.iterator();
        while((it1.hasNext())&&(it2.hasNext())){
            assertSame(it1.next(), it2.next());
        }
        assertFalse(it1.hasNext());
        assertFalse(it2.hasNext());
//        assertSame("Given objects aren't the same.", arr1, arr2);

            instance.add(o);
            instance.add(dict);        
            instance.add(arr1);
            instance.add(arr2);
            instance.add(o);
            instance.add(dict);             
            instance.add(arr1);
            instance.add(arr2);
            
            /* Check lastIndexOf */
            int expResult1 = 6;
            int expResult2 = 7;
            int result1 = instance.lastIndexOf(arr1);
            int result2 = instance.lastIndexOf(arr2);
            assertEquals("lastIndexOf: ", expResult1, result1);
            assertEquals("lastIndexOf: ", expResult2, result2);
            
            /* Check indexOf */
            int expResult3 = 2;
            int expResult4 = 3;
            int result3 = instance.indexOf(arr1);
            int result4 = instance.indexOf(arr2);
            assertEquals("indexOf: ", expResult3, result3);
            assertEquals("indexOf: ", expResult4, result4);

    }
}
