/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker annotation indicating a method in Action that actually runs the Action.
 * Used by {@code Runner} during run tree traversal. The method is called after the {@code @Init} marked method.
 * 
 * @author pasol01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Run {
    /**
     *
     * @return
     */
    boolean isCritical() default true;
    /**
     *
     * @return
     */
    int order() default 0;
}
