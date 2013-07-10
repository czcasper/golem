/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.annotations;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <A>
 * @author maslu02
 */
public class MethodAnnotationFieldComparator<A extends Annotation> implements Comparator<Method>, Serializable {

    private static final long serialVersionUID = 1L;
    protected Class<A> annotationClass;
    protected transient Method accesor;
    protected String fieldName;

    /**
     *
     * @param annotation
     * @param fieldName
     * @throws NoSuchMethodException
     */
    public MethodAnnotationFieldComparator(Class<A> annotation, String fieldName) throws NoSuchMethodException {
        if (annotation == null) {
            throw new NullPointerException("Annotation class cannot be null.");
        }
        if (fieldName == null) {
            throw new NullPointerException("Name of annotation field cannot be null.");
        }
        accesor = annotation.getMethod(fieldName);

        Class<?> fieldType = accesor.getReturnType();
        if ((fieldType.isAssignableFrom(Comparable.class)) || (fieldType.isPrimitive())) {
            this.fieldName = fieldName;
            annotationClass = annotation;
        }
    }

    @Override
    public int compare(Method o1, Method o2) {
        int retValue = 0;

        if (o1.isAnnotationPresent(annotationClass) && o2.isAnnotationPresent(annotationClass)) {
            try {
                Object objOne = accesor.invoke(o1.getAnnotation(annotationClass));
                Object objTwo = accesor.invoke(o2.getAnnotation(annotationClass));
                retValue = compareComparable(objOne, objTwo);

            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(MethodAnnotationFieldComparator.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (retValue == 0) {
                retValue = o1.getName().compareTo(o2.getName());
            }
        } else if (o1.isAnnotationPresent(annotationClass)) {
            retValue = -1;
        } else if (o2.isAnnotationPresent(annotationClass)) {
            retValue = 1;
        }

        return retValue;
    }

    protected final Method getFieldAccessor() throws NoSuchMethodException {
        if ((accesor == null) && (annotationClass != null)) {
            accesor = annotationClass.getMethod(fieldName);
        }
        return accesor;
    }

    private int compareComparable(Object o1, Object o2) {
        int retValue = 0;
        if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
            // TODO find correct way how to define this without warning about types            
            @SuppressWarnings("unchecked")
            Comparable<Comparable> c = (Comparable<Comparable>) o1;
            retValue = c.compareTo((Comparable) o2);
        } else if (o1 == null) {
            retValue = -1;
        } else if (o2 == null) {
            retValue = 1;
        }
        return retValue;
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        try {
            accesor = annotationClass.getMethod(fieldName);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new ClassNotFoundException("Cannot found field:" + fieldName + " in annotation class:" + annotationClass.getName(), ex);
        }
    }
}
