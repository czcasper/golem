/*
 */
package cz.a_d.automation.golem.spools.actions;

import cz.a_d.automation.golem.annotations.MethodAnnotationFieldComparator;
import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunParameter;
import cz.a_d.automation.golem.annotations.methods.Init;
import cz.a_d.automation.golem.annotations.methods.Run;
import cz.a_d.automation.golem.annotations.methods.Validate;
import cz.a_d.automation.golem.interfaces.context.ActionInfoProxy;
import cz.a_d.automation.golem.interfaces.spools.AbstractSpool;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import cz.a_d.automation.golem.spools.enums.ActionFieldProxyType;
import cz.a_d.automation.golem.spools.enums.ActionMethodProxyType;
import cz.a_d.automation.golem.spools.keys.SimpleActionInfoKey;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of action proxy information class. This class is realizing mapping of class or instances into actions if they are contains
 * annotations required by Golem actions.
 *
 * @author casper
 */
public class ActionInfoProxyImpl implements ActionInfoProxy {

    /**
     * Storage of all action fields sorted by type of field.
     */
    protected Map<ActionFieldProxyType, List<Field>> fieldsData = new EnumMap<>(ActionFieldProxyType.class);
    /**
     * Storage of all fields sorted by name of field.
     */
    protected Map<String, Field> fieldNameMap = new HashMap<>();
    /**
     * Storage for all action methods sorted by group and ordered by order defined by result of method comparator.
     */
    protected Map<ActionMethodProxyType, SortedSet<Method>> methodsData = new EnumMap<>(ActionMethodProxyType.class);
    /**
     * Storage for comparator used for specific group of method.
     */
    protected Map<ActionMethodProxyType, Comparator<Method>> methodComparators;

    /**
     * Construct instance of proxy object with defined comparators for methods.
     *
     * @param methodComparators instance of map which must have comparator instance stored under key type of group for all group supported
     *                          by proxy object.
     */
    public ActionInfoProxyImpl(Map<ActionMethodProxyType, Comparator<Method>> methodComparators) {
        if (methodComparators == null) {
            throw new NullPointerException("ActionInfo proxy cannot be initialized by null map");
        }
        this.methodComparators = methodComparators;
    }

    /**
     * Construct map of comparators with all supported types of action method group for proxy object.
     *
     * @return instance of map filled by method comparators.
     */
    public static Map<ActionMethodProxyType, Comparator<Method>> createNewComparators() {
        Map<ActionMethodProxyType, Comparator<Method>> retValue = new EnumMap<>(ActionMethodProxyType.class);
        try {
            retValue.put(ActionMethodProxyType.Initialize, new MethodAnnotationFieldComparator<>(Init.class, "order"));
            retValue.put(ActionMethodProxyType.Run, new MethodAnnotationFieldComparator<>(Run.class, "order"));
            retValue.put(ActionMethodProxyType.Validate, new MethodAnnotationFieldComparator<>(Validate.class, "order"));
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ActionInfoProxyImpl.class.getName()).log(Level.SEVERE, null, ex);
            retValue = null;
        }
        return retValue;
    }

    @Override
    public boolean addActionInfoProxy(ActionInfoProxy proxy) {
        boolean retValue = false;
        if (proxy != null) {
            Map<ActionFieldProxyType, List<Field>> fields = proxy.getFields();
            Map<String, Field> fieldNames = proxy.getFieldNames();
            if ((fields != null) && (!fields.isEmpty())) {
                fieldNameMap.putAll(fieldNames);
                for (Map.Entry<ActionFieldProxyType, List<Field>> entry : fields.entrySet()) {
                    List<Field> srcFields = entry.getValue();
                    ActionFieldProxyType type = entry.getKey();
                    if ((srcFields != null) && (!srcFields.isEmpty())) {
                        List<Field> dstFields = null;
                        if (fieldsData.containsKey(type)) {
                            dstFields = fieldsData.get(type);
                        }
                        if (dstFields != null) {
                            dstFields.addAll(srcFields);
                        } else {
                            fieldsData.put(type, srcFields);
                        }
                    }
                }
                retValue = true;
            }

            Map<ActionMethodProxyType, SortedSet<Method>> methods = proxy.getMethods();
            if ((methods != null) && (!methods.isEmpty())) {
                for (Map.Entry<ActionMethodProxyType, SortedSet<Method>> entry : methods.entrySet()) {
                    SortedSet<Method> srcMethods = entry.getValue();
                    ActionMethodProxyType type = entry.getKey();
                    if ((srcMethods != null) && (!srcMethods.isEmpty())) {
                        SortedSet<Method> dstMethods = null;
                        if (methodsData.containsKey(type)) {
                            dstMethods = methodsData.get(type);
                        }
                        if (dstMethods != null) {
                            dstMethods.addAll(srcMethods);
                        } else {
                            methodsData.put(type, srcMethods);
                        }
                    }
                }
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public boolean addField(ActionFieldProxyType type, Collection<Field> fields) {
        boolean retValue = false;
        if ((type != null) && (fields != null) && (!fields.isEmpty())) {
            Class<? extends Annotation> annotation = type.getAnnotation();
            if (annotation != null) {
                for (Field f : fields) {
                    if (f.isAnnotationPresent(annotation)) {
                        List<Field> fieldList;
                        if (fieldsData.containsKey(type)) {
                            fieldList = fieldsData.get(type);
                        } else {
                            fieldList = new ArrayList<>(fields.size());
                            fieldsData.put(type, fieldList);
                        }
                        fieldNameMap.put(f.getName(), f);
                        fieldList.add(f);
                        retValue = true;
                        if (type == ActionFieldProxyType.Parameters) {
                            if (f.getAnnotation(RunParameter.class).retValue()) {
                                List<Field> retValues;
                                if (fieldsData.containsKey(ActionFieldProxyType.ReturnValues)) {
                                    retValues = fieldsData.get(ActionFieldProxyType.ReturnValues);
                                } else {
                                    retValues = new ArrayList<>();
                                    fieldsData.put(ActionFieldProxyType.ReturnValues, retValues);
                                }
                                retValues.add(f);
                            }
                        }

                    }
                }
            }
        }
        return retValue;
    }

    @Override
    public List<Field> getField(ActionFieldProxyType type) {
        List<Field> retValue = null;
        if ((type != null) && (fieldsData.containsKey(type))) {
            retValue = fieldsData.get(type);
        }
        return retValue;
    }

    @Override
    public Field getField(String name) {
        Field retValue = null;
        if ((name != null) && (!name.isEmpty()) && (fieldNameMap.containsKey(name))) {
            retValue = fieldNameMap.get(name);
        }
        return retValue;
    }

    @Override
    public boolean addMethod(ActionMethodProxyType type, Collection<Method> methods) {
        boolean retValue = false;
        if ((type != null) && (methods != null) && (!methods.isEmpty()) && (methodComparators.containsKey(type))) {
            Class<? extends Annotation> annotation = type.getAnnotation();
            if (annotation != null) {
                for (Method m : methods) {
                    if (m.isAnnotationPresent(annotation)) {
                        SortedSet<Method> methodSet;
                        if (methodsData.containsKey(type)) {
                            methodSet = methodsData.get(type);
                        } else {
                            methodSet = new TreeSet<>(methodComparators.get(type));
                            methodsData.put(type, methodSet);
                        }
                        if (!methodSet.add(m)) {
                            Logger.getLogger(ActionInfoProxy.class.getName()).log(Level.WARNING, "Appended method with same name:{0} and with same order:{1}", new Object[]{m.getName(), annotation.toString()});
                        }
                        retValue = true;
                    }
                }
            }
        }
        return retValue;
    }

    @Override
    public SortedSet<Method> getMethod(ActionMethodProxyType type) {
        SortedSet<Method> retValue = null;
        if ((type != null) && (methodsData.containsKey(type))) {
            retValue = methodsData.get(type);
        }
        return retValue;
    }

    @Override
    public <A> boolean loadAction(Class<?> actionClass, AbstractSpool<A, ActionInfoKey<Class<?>>, ActionInfoProxy> loaded) {
        boolean retValue = false;
        try {
            if ((actionClass != null) && (actionClass.getConstructor() != null)) {
                ActionInfoKey<Class<?>> ak = new SimpleActionInfoKey(actionClass);
                ActionInfoProxyImpl tmpAction = new ActionInfoProxyImpl(methodComparators);

                while ((actionClass != null) && (!actionClass.equals(Object.class))) {
                    if ((loaded != null) && (loaded.containsKey(ak))) {
                        ActionInfoProxy load = loaded.get(ak);
                        tmpAction.addActionInfoProxy(load);
                        break;
                    }
                    if (actionClass.isAnnotationPresent(RunAction.class)) {
                        loadFields(actionClass, tmpAction.fieldsData, tmpAction.fieldNameMap);
                        loadMethods(actionClass, tmpAction.methodsData);
                    }
                    actionClass = actionClass.getSuperclass();
                    ak.set(actionClass);
                }
                if (tmpAction.isValid()) {
                    this.addActionInfoProxy(tmpAction);
                    retValue = true;
                }
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ActionInfoProxyImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retValue;
    }

    @Override
    public boolean isEmpty() {
        return ((fieldsData.isEmpty()) && (methodsData.isEmpty()));
    }

    @Override
    public boolean isValid() {
        boolean retValue = false;
        if (methodsData.containsKey(ActionMethodProxyType.Run)) {
            SortedSet<Method> tmp = methodsData.get(ActionMethodProxyType.Run);
            if ((tmp != null) && (!tmp.isEmpty())) {
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public void clear() {
        fieldNameMap.clear();
        fieldsData.clear();
        methodsData.clear();
    }

    @Override
    public Map<ActionFieldProxyType, List<Field>> getFields() {
        return fieldsData;
    }

    @Override
    public Map<ActionMethodProxyType, SortedSet<Method>> getMethods() {
        return methodsData;
    }

    @Override
    public Map<String, Field> getFieldNames() {
        return fieldNameMap;
    }

    /**
     * Loading fields from class into proxy object. Field are identified by using Golem annotations and sorted by type of this annotation.
     *
     * @param actionClass class which is valid action class.
     * @param fields      map of fields used to be filled by result of mapping.
     * @param names       map of field names used to be filled by result of mapping.
     */
    protected void loadFields(Class<?> actionClass, Map<ActionFieldProxyType, List<Field>> fields, Map<String, Field> names) {
        for (Field f : actionClass.getDeclaredFields()) {
            for (Annotation a : f.getDeclaredAnnotations()) {
                ActionFieldProxyType type = ActionFieldProxyType.getType(a.annotationType());
                if (type != null) {
                    names.put(f.getName(), f);
                    List<Field> dstFileds;
                    if (fields.containsKey(type)) {
                        dstFileds = fields.get(type);
                    } else {
                        dstFileds = new ArrayList<>();
                        fields.put(type, dstFileds);
                    }
                    dstFileds.add(f);
                    if (type == ActionFieldProxyType.Parameters) {
                        if (f.getAnnotation(RunParameter.class).retValue()) {
                            List<Field> retValues;
                            if (fieldsData.containsKey(ActionFieldProxyType.ReturnValues)) {
                                retValues = fieldsData.get(ActionFieldProxyType.ReturnValues);
                            } else {
                                retValues = new ArrayList<>();
                                fieldsData.put(ActionFieldProxyType.ReturnValues, retValues);
                            }
                            retValues.add(f);
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Loading methods from class into proxy object. Methods are identified by Golem annotations and grouped by annotation type. Sorting of
     * method in list is based on optional annotation parameter order.
     *
     * @param actionClass class which is valid action class.
     * @param methods     map of methods used to be filled by result of mapping.
     */
    protected void loadMethods(Class<?> actionClass, Map<ActionMethodProxyType, SortedSet<Method>> methods) {
        for (Method m : actionClass.getDeclaredMethods()) {
            for (Annotation a : m.getDeclaredAnnotations()) {
                ActionMethodProxyType type = ActionMethodProxyType.getType(a.annotationType());
                if (type != null) {
                    SortedSet<Method> dstMethods;
                    if (methods.containsKey(type)) {
                        dstMethods = methods.get(type);
                    } else if (methodComparators.containsKey(type)) {
                        dstMethods = new TreeSet<>(methodComparators.get(type));
                        methods.put(type, dstMethods);
                    } else {
                        dstMethods = null;
                        Logger.getLogger(ActionInfoProxyImpl.class.getName()).log(Level.WARNING, "Unknow comparator for type:{0} method:{1} will be ignored.", new Object[]{type, m.toString()});
                    }
                    if (dstMethods != null) {
                        dstMethods.add(m);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.fieldsData != null ? this.fieldsData.hashCode() : 0);
        hash = 71 * hash + (this.fieldNameMap != null ? this.fieldNameMap.hashCode() : 0);
        hash = 71 * hash + (this.methodsData != null ? this.methodsData.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActionInfoProxyImpl other = (ActionInfoProxyImpl) obj;

        if (this.fieldsData != other.fieldsData && (this.fieldsData == null || !this.fieldsData.equals(other.fieldsData))) {
            return false;
        }
        if (this.fieldNameMap != other.fieldNameMap && (this.fieldNameMap == null || !this.fieldNameMap.equals(other.fieldNameMap))) {
            return false;
        }
        return !(this.methodsData != other.methodsData && (this.methodsData == null || !this.methodsData.equals(other.methodsData)));
    }
}
