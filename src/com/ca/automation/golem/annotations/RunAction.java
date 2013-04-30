/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A marker annotation indicating class like processable in Golem.
 * Used by Golem's class {@code Runner} to execute logic defined inside class.
 * 
 * @author maslu02
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RunAction {
    /**
     * This property is for grouping action parameters. If more than one actions 
     * will use same group name, than fields with same names will be initialized 
     * by same objects.
     * 
     * By default simple class name is used.
     */
    String group() default "";
}
