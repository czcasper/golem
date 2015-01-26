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
