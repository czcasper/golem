/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.annotations.fields;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO actualize doc
/**
 * A marker annotation indicating field in an {@code xData} object that contains parameters.
 * Used by {@code Runner} during the data tree traversal and run tree construction.
 * 
 * @author pasol01
 */
//marker annotation which indicates which field of an xData object contains parameters (or method which returns parameters)

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunParameter {
    
    /**
     * Defines unique identifier for storing parameter values in runner's map
     * If empty, parameter name in format actionName.fieldName is automatically generated.
     * 
     * @return name of parameter
     */
    String name() default "";
    
    /**
     * Defines whether there is a value returned back to the map or not.
     * 
     * @return true if value is returned to the map
     */
    boolean retValue() default false;
    
    /**
     * Define id of object used for connecting value of field with stored value 
     * in parameter spool. If this atribute is non empty, golem try to use it 
     * in first step and if it is successfully found then name settings is ignored.
     * In case when pointer is not found, value from name attribute is involved 
     * into process.
     * 
     * @return if defined then non empty string, otherwise empty.
     */
    String pointer() default "";
    
}
