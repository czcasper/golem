/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//TODO improve doc
/**
 * A marker annotation indicating the {@code ActionData} class.
 * Used by {@code Runner} during data tree traversal.
 * 
 * @author pasol01
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RunnerAction {
    /**
     * 
     * @return
     */
    String name() default "";
}
