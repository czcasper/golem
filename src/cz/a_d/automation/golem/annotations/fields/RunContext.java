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
package cz.a_d.automation.golem.annotations.fields;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marker defined on scope of field and used by Golem to provide access to run-time context features. Annotation is used with
 * class type of action field to determine which context is requested to be injected into action.
 *
 * @author casper
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunContext {

    /**
     * Pointer to value which identify value in spool. Value in parameter map must be type of string or object which implements
     *
     * @class ConnectionKey interface.
     *
     * @return empty string if not defined, otherwise string which represent key
     */
    String pointer() default "";
}
