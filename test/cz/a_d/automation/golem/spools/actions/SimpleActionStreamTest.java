/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools.actions;

import cz.a_d.automation.golem.common.AddressArrayList;
import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.ActionInformationSpool;
import cz.a_d.automation.golem.interfaces.spools.ParameterSpool;
import cz.a_d.automation.golem.spools.ActionInformationSpoolImpl;
import cz.a_d.automation.golem.spools.ParameterSpoolImpl;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionForTestingContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithClone;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithConnection;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithNamedMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithPointerToContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithPointersOnMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithRetValues;
import cz.a_d.automation.testClasses.actions.dummy.valid.SimpleValidAction;
import cz.a_d.automation.testClasses.actions.dummy.valid.ValidActionWithNoRun;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author casper
 */
public class SimpleActionStreamTest {

    @Rule
    public ExpectedException testException = ExpectedException.none();
    protected List<Object> actionList;

    public SimpleActionStreamTest() {
        actionList = new ArrayList<>();
        actionList.add(new ActionForTestingContext());
        actionList.add(new ActionWithConnection());
        actionList.add(new ActionWithContext());
        actionList.add(new ActionWithMembers());
        actionList.add(new ActionWithNamedMembers());
        actionList.add(new ActionWithPointerToContext());
        actionList.add(new ActionWithPointersOnMembers());
        actionList.add(new ActionWithRetValues());
        actionList.add(new SimpleValidAction());
        actionList.add(new ValidActionWithNoRun());
    }

    /**
     * Test of SimpleActionStream class constructor called with null parameter
     * value, this usage must generate runtime exception NullPointerException.
     * Message expected in exception is part of test.
     */
    @Test
    public void testConstructor_Null() {
        testException.expect(NullPointerException.class);
        testException.expectMessage("Action stream cannot be initializet by null list of actions");
        SimpleActionStream<Object, Object> instance = new SimpleActionStream<>(null);
    }

    /**
     * Test of SimpleActionStream class constructor called with empty list in
     * parameter value, this usage must generate runtime exception
     * IllegalArgumentException. Message expected in exception is part of test.
     */
    @Test
    public void testConstructor_Empty() {
        testException.expect(IllegalArgumentException.class);
        testException.expectMessage("Action stream cannot be initializet by empty list of actions");
        SimpleActionStream<Object, Object> instance = new SimpleActionStream<>(new ArrayList<>());
    }

    /**
     * Test of SimpleActionStream class constructor called with different type
     * of List object then AddressArrayList. This type of List is only one
     * acceptable for correct work of stream. During object construction must be
     * different types converted into expected one.
     */
    @Test
    public void testConstructor_Conversion() {
        ArrayList<Object> dummy = new ArrayList<>(actionList);
        SimpleActionStream<Object, Object> instance = new SimpleActionStream<>(dummy);
        assertTrue(instance.actions instanceof AddressArrayList);
    }

    /**
     * Test of setParameterSpool method, of class SimpleActionStream. Validate
     * option to set null map into stream and functionality of setting valid
     * instance of parameter spool by getting value stored by instance.
     */
    @Test
    public void testSet_GetParameterSpool() {
        SimpleActionStream<Object, Object> instance = new SimpleActionStream<>(actionList);
        ParameterSpool<Object, Object> global = null;
        instance.setParameterSpool(global);
        assertNull(instance.getParameterSpool());
        global = ParameterSpoolImpl.getGlobal();
        instance.setParameterSpool(global);
        assertSame(global, instance.getParameterSpool());
    }

    /**
     * Test of resetableIterator method, of class SimpleActionStream. Validate
     * if resetable iterator covers whole list of actions initialized by
     * constructor.
     */
    @Test
    public void testResetableIterator() {
        SimpleActionStream<Object, Object> instance = new SimpleActionStream<>(actionList);
        ResetableIterator result = instance.resetableIterator();
        assertNotNull(result);
        Iterator<Object> it = actionList.iterator();
        while (it.hasNext()) {
            Object valid = it.next();
            assertTrue(result.hasNext());
            Object test = result.next();
            assertSame(valid, test);
        }
    }

    /**
     * Test of clone method, of class SimpleActionStream.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testClone() throws Exception {
        /**
         * Initialize and test clonning of stream with non clonable actions.
         */
        SimpleActionStream<Object, Object> instance = new SimpleActionStream<>(actionList);
        SimpleActionStream<Object, Object> result = (SimpleActionStream<Object, Object>) instance.clone();
        Iterator<Object> it = actionList.iterator();
        for (Object o : result.actions) {
            assertSame(it.next(), o);
        }
        
        /**
         * Initialize instance by clonable action and test functionality.
         */
        List<Object> tmpList = new ArrayList<>();
        tmpList.add(new ActionWithClone());
        instance = new SimpleActionStream<>(tmpList);
        result = (SimpleActionStream<Object, Object>) instance.clone();
        it = instance.actions.iterator();
        for(Object o : result.actions){
            Object t = it.next();
            assertNotSame(t, o);
        }        
    }
}
