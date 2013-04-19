/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker annotation indicating the method in Action that validates results of the {@code @Run} method.
 * Used by {@code Runner} during the run tree traversal.
 * The method is called after the {@code @Run} method.
 * @author pasol01
 */
//A marker annotation that indicates which method in the xRun object is used for validation of parameters
//Currently not used because the validation method is called as a part of the @Init method
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    /**
     *
     * @return
     */
    boolean isCritical() default true;
    /**
     *
     * @return
     */
    int order() default Integer.MIN_VALUE;
}
