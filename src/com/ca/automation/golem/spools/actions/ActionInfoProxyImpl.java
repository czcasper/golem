/*
 */
package com.ca.automation.golem.spools.actions;

import com.ca.automation.golem.annotations.MethodAnnotationFieldComparator;
import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.keys.ActionInfoKey;
import com.ca.automation.golem.spools.enums.ActionFieldProxyType;
import com.ca.automation.golem.spools.enums.ActionMethodProxyType;
import com.ca.automation.golem.spools.keys.SimpleActionInfoKey;
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
 *
 * @author maslu02
 */
public class ActionInfoProxyImpl implements ActionInfoProxy {

    protected Map<ActionFieldProxyType, List<Field>> fieldsData = new EnumMap<ActionFieldProxyType, List<Field>>(ActionFieldProxyType.class);
    protected Map<String, Field> fieldNameMap = new HashMap<String, Field>();
    protected Map<ActionMethodProxyType, SortedSet<Method>> methodsData = new EnumMap<ActionMethodProxyType, SortedSet<Method>>(ActionMethodProxyType.class);
    protected Map<ActionMethodProxyType, Comparator<Method>> methodComparators;

    public ActionInfoProxyImpl(Map<ActionMethodProxyType, Comparator<Method>> methodComparators) {
        if (methodComparators == null) {
            throw new NullPointerException("ActionInfo proxy cannot be initialized by null map");
        }
        this.methodComparators = methodComparators;
    }

    public static Map<ActionMethodProxyType, Comparator<Method>> createNewComparators() {
        Map<ActionMethodProxyType, Comparator<Method>> retValue = new EnumMap<ActionMethodProxyType, Comparator<Method>>(ActionMethodProxyType.class);
        try {
            retValue.put(ActionMethodProxyType.Initialize, new MethodAnnotationFieldComparator<Init>(Init.class, "order"));
            retValue.put(ActionMethodProxyType.Run, new MethodAnnotationFieldComparator<Run>(Run.class, "order"));
            retValue.put(ActionMethodProxyType.Validate, new MethodAnnotationFieldComparator<Validate>(Validate.class, "order"));
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
            Map<ActionMethodProxyType, SortedSet<Method>> methods = proxy.getMethods();
            if ((fields != null) && (!fields.isEmpty())) {
                fieldsData.putAll(fields);
                fieldNameMap.putAll(proxy.getFieldNames());
                retValue = true;
            }
            if ((methods != null) && (!methods.isEmpty())) {
                methodsData.putAll(methods);
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
                            fieldList = new ArrayList<Field>(fields.size());
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
                                    retValues = new ArrayList<Field>();
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
                            methodSet = new TreeSet<Method>(methodComparators.get(type));
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
        ActionInfoKey<Class<?>> ak = new SimpleActionInfoKey(actionClass);

        Map<ActionFieldProxyType, List<Field>> tmpfieldsData = new EnumMap<ActionFieldProxyType, List<Field>>(ActionFieldProxyType.class);
        Map<String, Field> tmpfieldNameMap = new HashMap<String, Field>();
        Map<ActionMethodProxyType, SortedSet<Method>> tmpmethodsData = new EnumMap<ActionMethodProxyType, SortedSet<Method>>(ActionMethodProxyType.class);

        while ((actionClass != null) && (!actionClass.equals(Object.class))) {
            if ((loaded != null) && (loaded.containsKey(ak))) {
                ActionInfoProxy load = loaded.get(ak);
                tmpfieldNameMap.putAll(load.getFieldNames());
                tmpfieldsData.putAll(load.getFields());
                tmpmethodsData.putAll(load.getMethods());
                break;
            }
            if (actionClass.isAnnotationPresent(RunAction.class)) {
                loadFields(actionClass, tmpfieldsData, tmpfieldNameMap);
                loadMethods(actionClass, tmpmethodsData);
            }
            actionClass = actionClass.getSuperclass();
            ak.set(actionClass);
        }
        if (tmpmethodsData.containsKey(ActionMethodProxyType.Run)) {
            SortedSet<Method> tmp = tmpmethodsData.get(ActionMethodProxyType.Run);
            if ((tmp != null) && (!tmp.isEmpty())) {
                methodsData.putAll(tmpmethodsData);
                fieldNameMap.putAll(tmpfieldNameMap);
                fieldsData.putAll(tmpfieldsData);
                retValue = true;
            }
        }
        return retValue;
    }

    @Override
    public boolean isEmpty() {
        return ((fieldsData.isEmpty()) && (methodsData.isEmpty()));
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
                        dstFileds = new ArrayList<Field>();
                        fields.put(type, dstFileds);
                    }
                    dstFileds.add(f);
                    if (type == ActionFieldProxyType.Parameters) {
                        if (f.getAnnotation(RunParameter.class).retValue()) {
                            List<Field> retValues;
                            if (fieldsData.containsKey(ActionFieldProxyType.ReturnValues)) {
                                retValues = fieldsData.get(ActionFieldProxyType.ReturnValues);
                            } else {
                                retValues = new ArrayList<Field>();
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

    protected void loadMethods(Class<?> actionClass, Map<ActionMethodProxyType, SortedSet<Method>> methods) {
        for (Method m : actionClass.getDeclaredMethods()) {
            for (Annotation a : m.getDeclaredAnnotations()) {
                ActionMethodProxyType type = ActionMethodProxyType.getType(a.annotationType());
                if (type != null) {
                    SortedSet<Method> dstMethods;
                    if (methods.containsKey(type)) {
                        dstMethods = methods.get(type);
                    } else if (methodComparators.containsKey(type)) {
                        dstMethods = new TreeSet<Method>(methodComparators.get(type));
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
        if (this.methodsData != other.methodsData && (this.methodsData == null || !this.methodsData.equals(other.methodsData))) {
            return false;
        }
        return true;
    }
}
