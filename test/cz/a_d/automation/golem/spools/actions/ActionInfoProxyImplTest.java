/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools.actions;

import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.spools.ActionInformationSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import cz.a_d.automation.golem.spools.ActionInformationSpoolImpl;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.enums.ActionMethodProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleActionInfoKey;
import cz.a_d.automation.testClasses.actions.dummy.ActionWithInit;
import cz.a_d.automation.testClasses.actions.dummy.ActionWithRun;
import cz.a_d.automation.testClasses.actions.dummy.ActionWithValidate;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithAfterInit;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithAfterRun;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithAfterValidate;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithBeforeInit;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithBeforeRun;
import cz.a_d.automation.testClasses.actions.dummy.ordering.ActionWithBeforeValidate;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithConnection;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithContext;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithMembers;
import cz.a_d.automation.testClasses.actions.dummy.valid.ActionWithRetValues;
import cz.a_d.automation.testClasses.actions.dummy.valid.ValidActionWithNoRun;
import cz.a_d.automation.testClasses.actions.wrong.WrongActionNoAnnots;
import cz.a_d.automation.testClasses.actions.wrong.WrongActionNoDefCOnstructor;
import cz.a_d.automation.testClasses.actions.wrong.WrongNoRunMethod;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author maslu02
 */
// TODO Refactoring: improve test scale related to test is methods are added correctly in case of multiple object relation ship.
public class ActionInfoProxyImplTest {

    protected Map<ActionMethodProxyType, Comparator<Method>> comparators = ActionInfoProxyImpl.createNewComparators();

    public ActionInfoProxyImplTest() {
    }

    /**
     * Test of createNewComparators method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testCreateNewComparators() {
        Map<ActionMethodProxyType, Comparator<Method>> result = ActionInfoProxyImpl.createNewComparators();
        assertNotNull(result);
        assertEquals(result.size(), ActionMethodProxyType.values().length);

        Map<ActionMethodProxyType, Comparator<Method>> result2 = ActionInfoProxyImpl.createNewComparators();
        assertNotSame(result2, result);

        assertArrayEquals(result2.keySet().toArray(), result.keySet().toArray());
        Iterator<Comparator<Method>> it1 = result.values().iterator();
        Iterator<Comparator<Method>> it2 = result2.values().iterator();
        while (it1.hasNext()) {
            assertEquals(it1.next().getClass(), it2.next().getClass());
        }
    }

    /**
     * Test of addActionInfoProxy method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testAddActionInfoProxy() {
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        assertEquals(instance.getFieldNames().size(), 0);
        assertEquals(instance.getFields().size(), 0);
        assertEquals(instance.getMethods().size(), 0);

        assertNull(instance.getField(ActionFieldProxyType.Connections));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));
        assertNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        boolean result = instance.addActionInfoProxy(null);
        assertFalse(result);

        ActionInfoProxy proxy = new ActionInfoProxyImpl(comparators);
        result = proxy.loadAction(ActionWithMembers.class, null);
        assertTrue(result);
        result = instance.addActionInfoProxy(proxy);
        assertTrue(result);

        assertEquals(instance.getFieldNames().size(), 2);
        assertEquals(instance.getFields().size(), 1);
        assertEquals(instance.getMethods().size(), 3);

        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNotNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNotNull(instance.getMethod(ActionMethodProxyType.Validate));
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));

        assertNull(instance.getField(ActionFieldProxyType.Connections));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));
    }

    /**
     * Test of addField method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testAddField() {
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        /**
         * Values for initial testin indicates correct work with null values.
         */
        ActionFieldProxyType type = null;
        Collection<Field> parameterFields = null;
        boolean result = instance.addField(type, parameterFields);
        assertFalse(result);

        /**
         * Testing first type of fied Connection.
         */
        type = ActionFieldProxyType.Connections;
        result = instance.addField(type, parameterFields);
        assertFalse(result);

        /**
         * Create list for parametrs annotated by RunParameterannotation.
         */
        parameterFields = new ArrayList<>();
        result = instance.addField(type, parameterFields);
        assertFalse(result);

        /**
         * Test if validation for annotation is working.
         */
        parameterFields.addAll(Arrays.asList(ActionWithMembers.class.getDeclaredFields()));
        result = instance.addField(type, parameterFields);
        assertFalse(result);
        assertNull(instance.getField(ActionFieldProxyType.Connections));
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Initialize and test fields with expected annotation RunConnection.
         */
        Collection<Field> connectionsField = new ArrayList<>();
        connectionsField.addAll(Arrays.asList(ActionWithConnection.class.getDeclaredFields()));

        result = instance.addField(type, connectionsField);
        assertTrue(result);
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Change tested type to new one, for defensive testing of same
         * functionality in class. And test validation for this specific type
         * agains parameter field type.
         */
        type = ActionFieldProxyType.Contexts;
        result = instance.addField(type, parameterFields);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Test validation for this specific type agains connection field type.
         */
        result = instance.addField(type, connectionsField);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Initialize field with expected annotations RunContext.
         */
        Collection<Field> contextField = new ArrayList<>();
        contextField.addAll(Arrays.asList(ActionWithContext.class.getDeclaredFields()));

        /**
         * Test if fields are added properly.
         */
        result = instance.addField(type, contextField);
        assertTrue(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Switch tested type to Pararameters. And test validation for this
         * specific type agains context field type.
         */
        type = ActionFieldProxyType.Parameters;
        result = instance.addField(type, contextField);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Test validation for this specific type agains connection field type.
         */
        result = instance.addField(type, connectionsField);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNull(instance.getField(ActionFieldProxyType.Parameters));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Test if fields are added properly. Fields doesn't contains any return
         * values.
         */
        result = instance.addField(type, parameterFields);
        assertTrue(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(instance.getField(ActionFieldProxyType.Parameters).size(), parameterFields.size());
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Initialize field with expected annotations RunContext.
         */
        Collection<Field> retValuesFields = new ArrayList<>();
        retValuesFields.addAll(Arrays.asList(ActionWithRetValues.class.getDeclaredFields()));

        /**
         * Test if fields annotated by RunParameter annotation with parameter
         * retValue set to true are added properly.
         */
        result = instance.addField(type, retValuesFields);
        assertTrue(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(instance.getField(ActionFieldProxyType.Parameters).size(), parameterFields.size() + retValuesFields.size());
        assertNotNull(instance.getField(ActionFieldProxyType.ReturnValues));
        assertEquals(instance.getField(ActionFieldProxyType.ReturnValues).size(), retValuesFields.size());

        /**
         * Swith to RetValue type to test if this type is protected properly.
         */
        type = ActionFieldProxyType.ReturnValues;

        /**
         * Test if valid fields with option retValue=true are ignored in this
         * way of specification.
         */
        result = instance.addField(type, retValuesFields);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(instance.getField(ActionFieldProxyType.Parameters).size(), parameterFields.size() + retValuesFields.size());
        assertNotNull(instance.getField(ActionFieldProxyType.ReturnValues));
        assertEquals(instance.getField(ActionFieldProxyType.ReturnValues).size(), retValuesFields.size());

        /**
         * Test protection agains field annotated by RunConnection.
         */
        result = instance.addField(type, connectionsField);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(instance.getField(ActionFieldProxyType.Parameters).size(), parameterFields.size() + retValuesFields.size());
        assertNotNull(instance.getField(ActionFieldProxyType.ReturnValues));
        assertEquals(instance.getField(ActionFieldProxyType.ReturnValues).size(), retValuesFields.size());

        /**
         * Test protection agains field annotated by RunContext.
         */
        result = instance.addField(type, contextField);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(instance.getField(ActionFieldProxyType.Parameters).size(), parameterFields.size() + retValuesFields.size());
        assertNotNull(instance.getField(ActionFieldProxyType.ReturnValues));
        assertEquals(instance.getField(ActionFieldProxyType.ReturnValues).size(), retValuesFields.size());

        /**
         * Test Protection agains field annotated by RunParameter without
         * retValue=true.
         */
        type = ActionFieldProxyType.ReturnValues;
        result = instance.addField(type, parameterFields);
        assertFalse(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Connections));
        assertEquals(instance.getField(ActionFieldProxyType.Connections).size(), connectionsField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Contexts));
        assertEquals(instance.getField(ActionFieldProxyType.Contexts).size(), contextField.size());
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(instance.getField(ActionFieldProxyType.Parameters).size(), parameterFields.size() + retValuesFields.size());
        assertNotNull(instance.getField(ActionFieldProxyType.ReturnValues));
        assertEquals(instance.getField(ActionFieldProxyType.ReturnValues).size(), retValuesFields.size());

        /**
         * Sepatate test for validation of loading fields with RunParameter
         * annotation and property retValue = true.
         */
        instance = new ActionInfoProxyImpl(comparators);
        parameterFields = new ArrayList<>();
        type = ActionFieldProxyType.Parameters;
        parameterFields.addAll(Arrays.asList(ActionWithRetValues.class.getDeclaredFields()));

        result = instance.addField(type, parameterFields);
        assertTrue(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(instance.getField(ActionFieldProxyType.Parameters).size(), parameterFields.size());
        assertNotNull(instance.getField(ActionFieldProxyType.ReturnValues));
        assertEquals(instance.getField(ActionFieldProxyType.ReturnValues).size(), parameterFields.size());
        assertNull(instance.getField(ActionFieldProxyType.Connections));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
    }

    /**
     * Test of getField method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testGetField_ActionFieldProxyType() {
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);

        /**
         * Null protection test.
         */
        ActionFieldProxyType type = null;
        List result = instance.getField(type);
        assertNull(result);

        /**
         * Testing if new instance doesnt contains information for field types.
         */
        for (ActionFieldProxyType t : ActionFieldProxyType.values()) {
            result = instance.getField(t);
            assertNull(result);

        }

        /**
         * Initialize instance by values for testing getFrom operations.
         */
        Collection<Field> contextField = new ArrayList<>();
        contextField.addAll(Arrays.asList(ActionWithContext.class.getDeclaredFields()));
        Collection<Field> connectionField = new ArrayList<>();
        connectionField.addAll(Arrays.asList(ActionWithConnection.class.getDeclaredFields()));
        Collection<Field> parametrField = new ArrayList<>();
        parametrField.addAll(Arrays.asList(ActionWithRetValues.class.getDeclaredFields()));
        boolean addField = instance.addField(ActionFieldProxyType.Contexts, contextField);
        assertTrue(addField);
        addField = instance.addField(ActionFieldProxyType.Connections, connectionField);
        assertTrue(addField);
        addField = instance.addField(ActionFieldProxyType.Parameters, parametrField);
        assertTrue(addField);

        /**
         * Test getting stored values for particular types currently supported
         * by golem.
         */
        type = ActionFieldProxyType.Parameters;
        result = instance.getField(type);
        assertNotNull(result);
        assertArrayEquals(parametrField.toArray(), result.toArray());

        type = ActionFieldProxyType.ReturnValues;
        result = instance.getField(type);
        assertNotNull(result);
        assertArrayEquals(parametrField.toArray(), result.toArray());

        type = ActionFieldProxyType.Connections;
        result = instance.getField(type);
        assertNotNull(result);
        assertArrayEquals(connectionField.toArray(), result.toArray());

        type = ActionFieldProxyType.Contexts;
        result = instance.getField(type);
        assertNotNull(result);
        assertArrayEquals(contextField.toArray(), result.toArray());
    }

    /**
     * Test of getField method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testGetField_String() {
        /**
         * Initial testing for protection agains null and empty parameter value.
         */
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        String name = null;
        Field result = instance.getField(name);
        assertNull(result);

        name = "";
        result = instance.getField(name);
        assertNull(result);

        /**
         * Initialize fields with fields annotated by RunParameter annotation.
         */
        Collection<Field> fields = new ArrayList<>();
        Field[] declaredFields = ActionWithMembers.class.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFields));

        /**
         * Initialize names of field and test if they are not existing in tested
         * instance.
         */
        List<String> names = new ArrayList<>(declaredFields.length);
        for (Field f : declaredFields) {
            name = f.getName();
            names.add(name);
            result = instance.getField(name);
            assertNull(result);
        }

        /**
         * Add fields into tested instance and validate success of operation.
         */
        boolean addField = instance.addField(ActionFieldProxyType.Parameters, fields);
        assertTrue(addField);
        assertNotNull(instance.getFieldNames());
        assertEquals(names.size(), instance.getFieldNames().size());

        /**
         * Validate if fields are stored properly by field name.
         */
        for (String s : names) {
            result = instance.getField(name);
            assertNotNull(result);
            assertEquals(name, result.getName());
        }

        /**
         * Initialize second array with other type of fields (RunConnection).
         * And test if field names are unique, it is pre-requirement of
         * following steps.
         */
        Collection<Field> connectionField = new ArrayList<>();
        connectionField.addAll(Arrays.asList(ActionWithConnection.class.getDeclaredFields()));
        for (Field f : ActionWithConnection.class.getDeclaredFields()) {
            names.add(f.getName());
            assertNull(instance.getField(f.getName()));
        }

        /**
         * Initialize instance by new ConnectionType of fields and test if they
         * are stored properly.
         */
        addField = instance.addField(ActionFieldProxyType.Connections, connectionField);
        assertTrue(addField);
        assertNotNull(instance.getFieldNames());
        assertEquals(names.size(), instance.getFieldNames().size());
        for (String s : names) {
            result = instance.getField(s);
            assertNotNull(result);
            assertEquals(s, result.getName());
        }
    }

    /**
     * Test of addMethod method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testAddMethod() {
        /**
         * Initialization of test variables and testing protection agains null
         * parameters.
         */
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        ActionMethodProxyType type = null;
        Collection<Method> methods = null;
        boolean result = instance.addMethod(type, methods);
        assertFalse(result);
        assertNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Initialize type to valid value and test null protection agains fields
         * parameter.
         */
        type = ActionMethodProxyType.Initialize;
        result = instance.addMethod(type, methods);
        assertFalse(result);
        assertNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Initialize variable for keeping invalid (not annotated methods) for
         * testing.
         */
        Collection<Method> wrongMethod = new ArrayList<>();
        wrongMethod.addAll(Arrays.asList(WrongActionNoAnnots.class.getDeclaredMethods()));

        /**
         * Test method annotation validation with wrong methods.
         */
        result = instance.addMethod(type, wrongMethod);
        assertFalse(result);
        assertNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Initialize variables for keeping valid methods used by following
         * testing steps. Class for testing must contains just methods annotated
         * by specific type of golem annotation.
         */
        Collection<Method> initMethod = new ArrayList<>();
        initMethod.addAll(Arrays.asList(ActionWithInit.class.getDeclaredMethods()));
        Collection<Method> runMethod = new ArrayList<>();
        runMethod.addAll(Arrays.asList(ActionWithRun.class.getDeclaredMethods()));
        Collection<Method> validMethod = new ArrayList<>();
        validMethod.addAll(Arrays.asList(ActionWithValidate.class.getDeclaredMethods()));

        /**
         * Test protection agains null type with valid methods.
         */
        result = instance.addMethod(null, validMethod);
        assertFalse(result);
        assertNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Test method protection agains incompatible annotations. type = Init
         * method annotations =
         *
         * @Validate
         */
        result = instance.addMethod(type, validMethod);
        assertFalse(result);
        assertNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Test method protection agains incompatible annotations. type = Init
         * method annotations =
         *
         * @Run
         */
        result = instance.addMethod(type, runMethod);
        assertFalse(result);
        assertNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Add valid methods into instance and test if they are properly added.
         */
        result = instance.addMethod(type, initMethod);
        assertTrue(result);
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertArrayEquals(initMethod.toArray(), instance.getMethod(ActionMethodProxyType.Initialize).toArray());
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Change tested type to run.
         */
        type = ActionMethodProxyType.Run;

        /**
         * Test method protection agains incompatible annotations. type = Run
         * method annotations =
         *
         * @Init
         */
        result = instance.addMethod(type, initMethod);
        assertFalse(result);
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertArrayEquals(initMethod.toArray(), instance.getMethod(ActionMethodProxyType.Initialize).toArray());
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Test method protection agains incompatible annotations. type = Run
         * method annotations =
         *
         * @Validate
         */
        result = instance.addMethod(type, validMethod);
        assertFalse(result);
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertArrayEquals(initMethod.toArray(), instance.getMethod(ActionMethodProxyType.Initialize).toArray());
        assertNull(instance.getMethod(ActionMethodProxyType.Run));
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Add valid methods into instance and test if they are properly added.
         */
        result = instance.addMethod(type, runMethod);
        assertTrue(result);
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertArrayEquals(initMethod.toArray(), instance.getMethod(ActionMethodProxyType.Initialize).toArray());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Run));
        assertArrayEquals(runMethod.toArray(), instance.getMethod(ActionMethodProxyType.Run).toArray());
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Changed tested type to Validate.
         */
        type = ActionMethodProxyType.Validate;

        /**
         * Test method protection agains incompatible annotations. type =
         * Validate method annotations =
         *
         * @Init
         */
        result = instance.addMethod(type, initMethod);
        assertFalse(result);
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertArrayEquals(initMethod.toArray(), instance.getMethod(ActionMethodProxyType.Initialize).toArray());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Run));
        assertArrayEquals(runMethod.toArray(), instance.getMethod(ActionMethodProxyType.Run).toArray());
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Test method protection agains incompatible annotations. type =
         * Validate method annotations =
         *
         * @Run
         */
        result = instance.addMethod(type, runMethod);
        assertFalse(result);
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertArrayEquals(initMethod.toArray(), instance.getMethod(ActionMethodProxyType.Initialize).toArray());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Run));
        assertArrayEquals(runMethod.toArray(), instance.getMethod(ActionMethodProxyType.Run).toArray());
        assertNull(instance.getMethod(ActionMethodProxyType.Validate));

        /**
         * Add valid methods into instance and test if they are properly added.
         */
        result = instance.addMethod(type, validMethod);
        assertTrue(result);
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertArrayEquals(initMethod.toArray(), instance.getMethod(ActionMethodProxyType.Initialize).toArray());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Run));
        assertArrayEquals(runMethod.toArray(), instance.getMethod(ActionMethodProxyType.Run).toArray());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Validate));
        assertArrayEquals(validMethod.toArray(), instance.getMethod(ActionMethodProxyType.Validate).toArray());

    }

    /**
     * Test of getMethod method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testGetMethod() {
        /**
         * Initial testing of protection agains null parameters.
         */
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        ActionMethodProxyType type = null;
        SortedSet<Method> result = instance.getMethod(type);
        assertNull(result);
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            assertNull(instance.getMethod(t));
        }

        /**
         * Initialize variables for keeping valid methods used by following
         * testing steps. Class for testing must contains just methods annotated
         * by specific type of golem annotation.
         */
        Map<ActionMethodProxyType, Collection<Method>> testMethods = new EnumMap<>(ActionMethodProxyType.class);
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            Collection<Method> methods;
            switch (t) {
                case Initialize:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithInit.class.getDeclaredMethods()));
                    break;
                case Run:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithRun.class.getDeclaredMethods()));
                    break;
                case Validate:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithValidate.class.getDeclaredMethods()));
                    break;
                default:
                    methods = null;
            }
            if (methods != null) {
                testMethods.put(t, methods);
            }
        }
        assertEquals(ActionMethodProxyType.values().length, testMethods.size());

        /**
         * Add initialized methods into tested instance.
         */
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            boolean addMethod = instance.addMethod(t, testMethods.get(t));
            assertTrue(addMethod);

        }

        /**
         * Testing if all supported methods are returned correctly.
         */
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            result = instance.getMethod(t);
            assertNotNull(result);
            assertArrayEquals(testMethods.get(t).toArray(), result.toArray());
        }

        /**
         * Test getFrom method and validate ordering of method by annotation
         * parameter order. Initialize test methods which should be used befor
         * default values.
         */
        Map<ActionMethodProxyType, Collection<Method>> beforeTestMethods = new EnumMap<>(ActionMethodProxyType.class);
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            Collection<Method> methods;
            switch (t) {
                case Initialize:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithBeforeInit.class.getDeclaredMethods()));
                    break;
                case Run:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithBeforeRun.class.getDeclaredMethods()));
                    break;
                case Validate:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithBeforeValidate.class.getDeclaredMethods()));
                    break;
                default:
                    methods = null;
            }
            if (methods != null) {
                beforeTestMethods.put(t, methods);
            }
        }
        assertEquals(ActionMethodProxyType.values().length, beforeTestMethods.size());

        /**
         * Initialize methods which should be used after default method.
         */
        Map<ActionMethodProxyType, Collection<Method>> afterTestMethods = new EnumMap<>(ActionMethodProxyType.class);
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            Collection<Method> methods;
            switch (t) {
                case Initialize:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithAfterInit.class.getDeclaredMethods()));
                    break;
                case Run:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithAfterRun.class.getDeclaredMethods()));
                    break;
                case Validate:
                    methods = new ArrayList<>();
                    methods.addAll(Arrays.asList(ActionWithAfterValidate.class.getDeclaredMethods()));
                    break;
                default:
                    methods = null;
            }
            if (methods != null) {
                afterTestMethods.put(t, methods);
            }
        }
        assertEquals(ActionMethodProxyType.values().length, afterTestMethods.size());

        /**
         * Add initialized methods into tested instance.
         */
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            boolean addMethod = instance.addMethod(t, afterTestMethods.get(t));
            assertTrue(addMethod);
        }
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            boolean addMethod = instance.addMethod(t, beforeTestMethods.get(t));
            assertTrue(addMethod);
        }

        /**
         * Testing if all supported methods are returned correctly.
         */
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            result = instance.getMethod(t);
            assertNotNull(result);
            Iterator<Method> it = result.iterator();
            for (Method m : beforeTestMethods.get(t)) {
                assertEquals(m, it.next());
            }
            for (Method m : testMethods.get(t)) {
                assertEquals(m, it.next());
            }
            for (Method m : afterTestMethods.get(t)) {
                assertEquals(m, it.next());
            }
        }

    }

    /**
     * Test of loadAction method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testLoadAction() {
        /**
         * Initialize test instance and test protection agans null parameter.
         */
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        Class<?> testActionClass = null;
        boolean result = instance.loadAction(testActionClass, null);
        assertFalse(result);
        for (ActionFieldProxyType t : ActionFieldProxyType.values()) {
            assertNull(instance.getField(t));
        }
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            assertNull(instance.getMethod(t));
        }

        /**
         * Test protection agains object without annotations.
         */
        testActionClass = WrongActionNoAnnots.class;
        result = instance.loadAction(testActionClass, null);
        assertFalse(result);
        for (ActionFieldProxyType t : ActionFieldProxyType.values()) {
            assertNull(instance.getField(t));
        }
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            assertNull(instance.getMethod(t));
        }

        /**
         * Test protection agains actions without defined run method.
         */
        testActionClass = WrongNoRunMethod.class;
        result = instance.loadAction(testActionClass, null);
        assertFalse(result);
        for (ActionFieldProxyType t : ActionFieldProxyType.values()) {
            assertNull(instance.getField(t));
        }
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            assertNull(instance.getMethod(t));
        }

        /**
         * Test protection agains action without default empty constructor.
         */
        testActionClass = WrongActionNoDefCOnstructor.class;
        result = instance.loadAction(testActionClass, null);
        assertFalse(result);
        for (ActionFieldProxyType t : ActionFieldProxyType.values()) {
            assertNull(instance.getField(t));
        }
        for (ActionMethodProxyType t : ActionMethodProxyType.values()) {
            assertNull(instance.getMethod(t));
        }        
        
        /**
         * Test loading action with members and methods.
         */
        testActionClass = ActionWithMembers.class;
        result = instance.loadAction(testActionClass, null);
        assertTrue(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertArrayEquals(ActionWithMembers.class.getDeclaredFields(), instance.getField(ActionFieldProxyType.Parameters).toArray());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertEquals(1, instance.getMethod(ActionMethodProxyType.Initialize).size());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Run));
        assertEquals(1, instance.getMethod(ActionMethodProxyType.Run).size());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Validate));
        assertEquals(1, instance.getMethod(ActionMethodProxyType.Validate).size());
        assertNull(instance.getField(ActionFieldProxyType.Connections));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Initialize spool of actions for testing feature of acceleration of
         * loading by using information from previously loaded actions.
         */
        ActionInformationSpool<Object> testingSpool = new ActionInformationSpoolImpl<>();
        ActionInfoKey<Class<?>> key = new SimpleActionInfoKey(testActionClass);
        ActionInfoProxy put = testingSpool.put(key, instance);
        assertNull(put);

        /**
         * Create new instance and fromString without using spool cache into test
         * instance.
         */
        instance = new ActionInfoProxyImpl(comparators);
        testActionClass = ValidActionWithNoRun.class;
        result = instance.loadAction(testActionClass, null);
        assertTrue(result);
        assertNotNull(instance.getField(ActionFieldProxyType.Parameters));
        assertEquals(ActionWithMembers.class.getDeclaredFields().length + ValidActionWithNoRun.class.getDeclaredFields().length, instance.getField(ActionFieldProxyType.Parameters).size());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Initialize));
        assertEquals(2, instance.getMethod(ActionMethodProxyType.Initialize).size());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Run));
        assertEquals(1, instance.getMethod(ActionMethodProxyType.Run).size());
        assertNotNull(instance.getMethod(ActionMethodProxyType.Validate));
        assertEquals(2, instance.getMethod(ActionMethodProxyType.Validate).size());
        assertNull(instance.getField(ActionFieldProxyType.Connections));
        assertNull(instance.getField(ActionFieldProxyType.Contexts));
        assertNull(instance.getField(ActionFieldProxyType.ReturnValues));

        /**
         * Initialize second instance for testing loading result with cache.
         */
        ActionInfoProxy instance2 = new ActionInfoProxyImpl(comparators);
        testActionClass = ValidActionWithNoRun.class;
        result = instance2.loadAction(testActionClass, testingSpool);
        assertTrue(result);
        assertNotNull(instance2.getField(ActionFieldProxyType.Parameters));
        assertEquals(ActionWithMembers.class.getDeclaredFields().length + ValidActionWithNoRun.class.getDeclaredFields().length, instance2.getField(ActionFieldProxyType.Parameters).size());
        assertNotNull(instance2.getMethod(ActionMethodProxyType.Initialize));
        assertEquals(2, instance2.getMethod(ActionMethodProxyType.Initialize).size());
        assertNotNull(instance2.getMethod(ActionMethodProxyType.Run));
        assertEquals(1, instance2.getMethod(ActionMethodProxyType.Run).size());
        assertNotNull(instance2.getMethod(ActionMethodProxyType.Validate));
        assertEquals(2, instance2.getMethod(ActionMethodProxyType.Validate).size());
        assertNull(instance2.getField(ActionFieldProxyType.Connections));
        assertNull(instance2.getField(ActionFieldProxyType.Contexts));
        assertNull(instance2.getField(ActionFieldProxyType.ReturnValues));

        assertTrue(instance.equals(instance2));
    }

    /**
     * Test of getFields method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testGetFields() {
        /**
         * Simple testing of method. This method is tested by other tests.
         */
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        Map<ActionFieldProxyType, List<Field>> result = instance.getFields();
        assertNotNull(result);
        assertEquals(0, result.size());

        /**
         * Load class with one type of parameter.
         */
        boolean add = instance.loadAction(ActionWithMembers.class, null);
        assertTrue(add);

        /**
         * Test if classes are loaded correctly.
         */
        result = instance.getFields();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertArrayEquals(ActionWithMembers.class.getDeclaredFields(), result.get(ActionFieldProxyType.Parameters).toArray());

        add = instance.loadAction(ActionWithConnection.class, null);
        assertTrue(add);
        result = instance.getFields();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertArrayEquals(ActionWithConnection.class.getDeclaredFields(), result.get(ActionFieldProxyType.Connections).toArray());

        add = instance.loadAction(ActionWithContext.class, null);
        assertTrue(add);
        result = instance.getFields();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertArrayEquals(ActionWithContext.class.getDeclaredFields(), result.get(ActionFieldProxyType.Contexts).toArray());

        add = instance.loadAction(ActionWithRetValues.class, null);
        assertTrue(add);
        result = instance.getFields();
        assertNotNull(result);
        assertEquals(4, result.size());
        assertArrayEquals(ActionWithRetValues.class.getDeclaredFields(), result.get(ActionFieldProxyType.ReturnValues).toArray());
    }

    /**
     * Test of getMethods method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testGetMethods() {
        /**
         * Simple getMethod validation.
         */
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        Map<ActionMethodProxyType, SortedSet<Method>> result = instance.getMethods();
        assertNotNull(result);
        assertEquals(0, result.size());

        boolean loadAction = instance.loadAction(ActionWithMembers.class, null);
        assertTrue(loadAction);
        result = instance.getMethods();
        assertNotNull(result);
        assertEquals(3, result.size());

        /**
         * Test clear method and is empty methods.
         */
        instance.clear();
        result = instance.getMethods();
        assertNotNull(result);
        assertEquals(0, result.size());

        assertTrue(instance.isEmpty());
    }

    /**
     * Test of getFieldNames method, of class ActionInfoProxyImpl.
     */
    @Test
    public void testGetFieldNames() {
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        Map<String, Field> result = instance.getFieldNames();
        assertNotNull(result);
        assertEquals(0, result.size());
        assertTrue(instance.isEmpty());
        boolean loadAction = instance.loadAction(ActionWithRetValues.class, null);
        assertTrue(loadAction);

        result = instance.getFieldNames();
        assertNotNull(result);
        assertEquals(ActionWithRetValues.class.getDeclaredFields().length, result.size());
    }

    /**
     * Simple test for testing two methods from ActionProxy interface.
     */
    @Test
    public void testIsEmptyandClear() {
        ActionInfoProxy instance = new ActionInfoProxyImpl(comparators);
        assertTrue(instance.isEmpty());

        instance.clear();
        assertTrue(instance.isEmpty());

        instance.loadAction(ValidActionWithNoRun.class, null);
        assertFalse(instance.isEmpty());

        instance.clear();
        assertTrue(instance.isEmpty());
    }
}
