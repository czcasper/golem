/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.annotations.fields;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marker defined on scope of field to identify fields which value should be managed by Golem. Action field marked with this
 * annotation are injected before execution of method and returned if is requested after execution.
 *
 * @author casper
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunParameter {

    /**
     * Defines unique identifier for storing parameter values in runner's map If empty, parameter name in format actionName.fieldName is
     * automatically generated.
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
     * Define id of object used for connecting value of field with stored value in parameter spool. If this atribute is non empty, golem try
     * to use it in first step and if it is successfully found then name settings is ignored. In case when pointer is not found, value from
     * name attribute is involved into process.
     *
     * @return if defined then non empty string, otherwise empty.
     */
    String pointer() default "";

}
