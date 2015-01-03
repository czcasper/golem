/*
 */
package cz.a_d.automation.golem.spools.enums;

import cz.a_d.automation.golem.annotations.fields.RunConnection;
import cz.a_d.automation.golem.annotations.fields.RunContext;
import cz.a_d.automation.golem.annotations.fields.RunParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author casper
 */
public enum ActionFieldProxyType {
    /**
     * Represents parameter type of action fields. This type of fields are injected before action methods are executed.
     */
    Parameters,
    /**
     * Represents connection type of action fields. This type of fields are injected before action methods are executed.
     */
    Connections,
    /**
     * Represents parameters returned after processing from value of action fields. This type of fields are retrieved after action methods
     * are executed.
     */
    ReturnValues,
    /**
     * Represents Golem runtime context object type of action fields. This type of fields are injected before action methods are executed.
     */
    Contexts;

    /**
     * Getter to identify type of field from Field class.
     *
     * @param field instance of Field which will be used to search for annotations connected with Golem field types.
     * @return instance of type in case when field is valid action field, otherwise false.
     */
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

    /**
     * Getter to identify type of field from Annotation class.
     *
     * @param annotation instance of annotation which will be compared with supported annotation types.
     * @return instance of proxy type in case when annotation is supported, otherwise null.
     */
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

    /**
     * Getter to return annotation from current enumeration value.
     *
     * @return annotation class connected with this specific value.
     */
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

    /**
     * Getting value stored in field property pointer.
     *
     * @param f instance of field used to retrieve value of pointer parameter.
     * @return value defined in annotation pointer property if field is valid action field, otherwise null.
     */
    public String getPointer(Field f) {
        String retValue = null;
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
                    retValue = null;
            }
        }
        return retValue;
    }

    /**
     * Getting value stored in field property name.
     *
     * @param f instance of field used to retrieve value of name parameter.
     * @return value defined in annotation name property if field is valid action field, otherwise null.
     */
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
