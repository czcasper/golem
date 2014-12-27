/*
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
 * Implements comparing of methods based on value defined in method annotation. This allows to sort methods into order defined by developer
 * of action and keep this order consistent during action execution.
 *
 * @author casper
 * @param <A> annotation class type used for comparing method objects. This annotation must be defined on method level and will be searched
 *            in method object during compare process.
 */
public class MethodAnnotationFieldComparator<A extends Annotation> implements Comparator<Method>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Annotation class is used to comparing of methods.
     */
    protected Class<A> annotationClass;

    /**
     * Class method which provide access to value defined in annotation field. Must be transient to allow serialization of comparator.
     */
    protected transient Method accesor;

    /**
     * Name of annotation field used for comparing methods.
     */
    protected String fieldName;

    /**
     * Construct comparator with specified annotation type and annotation field name for comparison.
     *
     * @param annotation class of annotation searched on method level and used for comparison.
     * @param fieldName  name of annotation parameter, value of this field is used for comparison and needs to be primitive type or object
     *                   implementing comparable interface.
     *
     * @throws NoSuchMethodException in case when annotation doesn't have field with requested name.
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

    /**
     * Comparing two methods with or without requested annotations. In case when both methods doesn't have annotation present then they are
     * considered as same. In case when just one object has defined requested annotation then method without annotation is bigger than
     * method with defined annotation.
     *
     * @param o1 the first method to be compared.
     * @param o2 the second method to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
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

    /**
     * Compares two annotation field values. Fields object must be able to successfully cast to Comparable interface.
     *
     * @param o1 the first field to be compared.
     * @param o2 the second field to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    protected int compareComparable(Object o1, Object o2) {
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

    /**
     * Method called during loading object from serialized state into memory. This method is used for initialization of transient fields in
     * this case access method.
     *
     * @param s
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
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
