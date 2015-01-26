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
package cz.a_d.automation.golem.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A marker annotation indicating class like processable in Golem.
 * Used by Golem's class {@code Runner} to execute logic defined inside class.
 * 
 * @author casper
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RunAction {
    /**
     * This property is for grouping action parameters. If more than one actions 
     * will use same group name, than fields with same names will be initialized 
     * by same objects.
     * 
     * By default simple class name is used.
     * @return group name.
     */
    String group() default "";
}
