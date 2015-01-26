/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.annotations.methods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marker defined on scope of method to identify method which will be executed in first group. This group is representing logical
 * block of initialization before main action business logic will be executed. Annotation can be used multiple times and method order will
 * be determined by value of order field.
 *
 * @author casper
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {
    /**
     * Marking method like critical for action. This will lead to validation of result after method and if it will fail than rest of methods
     * defined by action is skipped.
     *
     * @return value of marker true in case when method is mandatory otherwise false.
     */
    boolean isCritical() default true;

    /**
     * Method execution order index. Default value is 0. This property is used to sort methods of action before execution. It also giving to
     * action programer option chance to control order of methods execution.
     *
     * @return integer value of order defined by action programer on method by annotation.
     */
    int order() default 0;
}
