/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO some value
/**
 * A marker annotation indicating method for loading parameters into Action.
 * Used by {@code Runner} during RunTree traversal.
 * It is the first method called after the action construction.
 * 
 * @author pasol01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {
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
