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
package cz.a_d.automation.golem.spools.enums;

import cz.a_d.automation.golem.annotations.methods.Init;
import cz.a_d.automation.golem.annotations.methods.Run;
import cz.a_d.automation.golem.annotations.methods.Validate;
import java.lang.annotation.Annotation;

/**
 *
 * @author casper
 */
public enum ActionMethodProxyType {
    /**
     * Represents initialization group of action methods. This type of methods are called before action method are executed.
     */
    Initialize,
    /**
     * Represents main group of action methods. This type of methods are implementing main business logic of action.
     */
    Run,
    /**
     * Represents validation group of action methods. This type of methods are called after action method are executed.
     */
    Validate;

    /**
     * Getter to identify type of method from annotation class.
     *
     * @param annotation instance of annotation which will be compared with supported annotation types.
     * @return instance of proxy type in case when annotation is supported, otherwise null.
     */
    public static ActionMethodProxyType getType(Class<? extends Annotation> annotation) {
        ActionMethodProxyType retValue = null;
        if (annotation != null) {
            if (Run.class.equals(annotation)) {
                retValue = Run;
            } else if (Init.class.equals(annotation)) {
                retValue = Initialize;
            } else if (Validate.class.equals(annotation)) {
                retValue = Validate;
            }
        }
        return retValue;
    }

    /**
     * Getting value stored in field property isCritical.
     *
     * @param annotation instance of annotation used to retrieve value of isCritical parameter.
     * @return value defined in annotation isCritical property if method is valid action method, otherwise null.
     */
    public boolean isCritical(Annotation annotation) {
        boolean retValue = false;
        if (annotation != null) {
            switch (this) {
                case Initialize:
                    if (annotation instanceof Init) {
                        retValue = ((Init) annotation).isCritical();
                    } else {
                        throw new IllegalArgumentException("Used argument was has incompatible type:" + annotation.annotationType() + " expected type for:" + Initialize + " is " + Init.class);
                    }
                    break;
                case Run:
                    if (annotation instanceof Run) {
                        retValue = ((Run) annotation).isCritical();
                    } else {
                        throw new IllegalArgumentException("Used argument was has incompatible type:" + annotation.annotationType() + " expected type for:" + Run + " is " + Run.class);
                    }
                    break;
                case Validate:
                    if (annotation instanceof Validate) {
                        retValue = ((Validate) annotation).isCritical();
                    } else {
                        throw new IllegalArgumentException("Used argument was has incompatible type:" + annotation.annotationType() + " expected type for:" + Validate + " is " + Validate.class);
                    }
                    break;
            }
        }
        return retValue;
    }

    /**
     * Getter to return annotation from current enumeration value.
     *
     * @return annotation class connected with this specific value.
     */
    public Class<? extends Annotation> getAnnotation() {
        Class<? extends Annotation> retValue = null;
        switch (this) {
            case Initialize:
                retValue = Init.class;
                break;
            case Run:
                retValue = Run.class;
                break;
            case Validate:
                retValue = Validate.class;
                break;
        }
        return retValue;
    }
}
