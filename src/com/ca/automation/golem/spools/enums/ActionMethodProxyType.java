/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.enums;

import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maslu02
 */
public enum ActionMethodProxyType {

    Initialize, Run, Validate;
    
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
