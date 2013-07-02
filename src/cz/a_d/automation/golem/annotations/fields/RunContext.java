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
 *
 * @author maslu02
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunContext {

    /**
     * Pointer to value which identify value in spool. Value in parameter map
     * must be type of string or object which implements
     *
     * @class ConnectionKey interface.
     *
     * @return empty string if not defined, otherwise string which represent key
     */
    String pointer() default "";
}
