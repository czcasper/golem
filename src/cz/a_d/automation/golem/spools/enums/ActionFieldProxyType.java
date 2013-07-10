/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.spools.enums;

import cz.a_d.automation.golem.annotations.fields.RunConnection;
import cz.a_d.automation.golem.annotations.fields.RunContext;
import cz.a_d.automation.golem.annotations.fields.RunParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author maslu02
 */
public enum ActionFieldProxyType {

    Parameters, Connections, ReturnValues, Contexts;

    public static ActionFieldProxyType getType(Field field) {
        ActionFieldProxyType retValue = null;
        if (field != null) {
            for (Annotation a : field.getDeclaredAnnotations()) {
                retValue = getType(a.annotationType());
                if (retValue != null) {
                    break;
                }
            }
        }
        return retValue;
    }

    public static ActionFieldProxyType getType(Class<? extends Annotation> annotation) {
        ActionFieldProxyType retValue = null;
        if (annotation != null) {
            if (RunParameter.class.equals(annotation)) {
                retValue = Parameters;
            } else if (RunConnection.class.equals(annotation)) {
                retValue = Connections;
            } else if (RunContext.class.equals(annotation)) {
                retValue = Contexts;
            }
        }
        return retValue;
    }

    public Class<? extends Annotation> getAnnotation() {
        Class<? extends Annotation> retValue = null;
        switch (this) {
            case Parameters:
                retValue = RunParameter.class;
                break;
            case Connections:
                retValue = RunConnection.class;
                break;
            case Contexts:
                retValue = RunContext.class;
                break;
            case ReturnValues:
                retValue = null;
                break;
        }
        return retValue;
    }

    public String getPointer(Field f) {
        String retValue=null;
        if (f != null) {
            switch (this) {
                case Parameters:
                    if (f.isAnnotationPresent(RunParameter.class)) {
                        retValue = f.getAnnotation(RunParameter.class).pointer();
                    }
                    break;
                case Connections:
                    if (f.isAnnotationPresent(RunConnection.class)) {
                        retValue = f.getAnnotation(RunConnection.class).pointer();
                    }
                    break;
                case Contexts:
                    if (f.isAnnotationPresent(RunContext.class)) {
                        retValue = f.getAnnotation(RunContext.class).pointer();
                    }
                    break;
                default:
                    retValue=null;
            }
        }
        return retValue;
    }

    public String getName(Field f) {
        String retValue = null;
        if (f != null) {
            switch (this) {
                case Connections:
                    if (f.isAnnotationPresent(RunConnection.class)) {
                        retValue = f.getAnnotation(RunConnection.class).name();
                    }
                    break;
                case Parameters:
                    if (f.isAnnotationPresent(RunParameter.class)) {
                        retValue = f.getAnnotation(RunParameter.class).name();
                    }
                    break;
                default:
                    retValue = null;
            }
        }
        return retValue;
    }
}
