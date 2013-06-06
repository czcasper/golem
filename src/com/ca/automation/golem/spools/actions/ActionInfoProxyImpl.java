/*
 */
package com.ca.automation.golem.spools.actions;

import com.ca.automation.golem.spools.enums.ActionFieldProxyType;
import com.ca.automation.golem.spools.enums.ActionMethodProxyType;
import com.ca.automation.golem.interfaces.context.ActionInfoProxy;
import com.ca.automation.golem.annotations.MethodAnnotationFieldComparator;
import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import com.ca.automation.golem.interfaces.spools.AbstractSpool;
import com.ca.automation.golem.interfaces.spools.keys.ActionInfoKey;
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
        if(methodComparators==null){
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
            List<Field> fieldList;
            if (fieldsData.containsKey(type)) {
                fieldList = fieldsData.get(type);
            } else {
                fieldList = new ArrayList<Field>(fields.size());
                fieldsData.put(type, fieldList);
            }
            for (Field f : fields) {
                fieldNameMap.put(f.getName(), f);
                fieldList.add(f);
            }
            retValue = true;
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
            SortedSet<Method> methodList;
            if (methodsData.containsKey(type)) {
                methodList = methodsData.get(type);
            } else {
                methodList = new TreeSet<Method>(methodComparators.get(type));
                methodsData.put(type, methodList);
            }
            methodList.addAll(methods);
            retValue = true;
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
        while ((actionClass != null) && (!actionClass.equals(Object.class))) {
            if ((loaded.containsKey(ak))) {
                addActionInfoProxy(loaded.get(ak));
                break;
            }
            if (actionClass.isAnnotationPresent(RunAction.class)) {
                loadFields(actionClass);
                loadMethods(actionClass);
            }
            actionClass = actionClass.getSuperclass();
            ak.set(actionClass);
        }
        if (methodsData.containsKey(ActionMethodProxyType.Run)) {
            SortedSet<Method> tmp = methodsData.get(ActionMethodProxyType.Run);
            if ((tmp != null) && (!tmp.isEmpty())) {
                retValue = true;
            }
        }
        return retValue;
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

    protected void loadFields(Class<?> actionClass) {
        Map<Class<?>, ActionFieldProxyType> annotationMap = ActionFieldProxyType.getAnnotationMap();
        for (Field f : actionClass.getDeclaredFields()) {
            for (Annotation a : f.getDeclaredAnnotations()) {
                if (annotationMap.containsKey(a.getClass())) {
                    fieldNameMap.put(f.getName(), f);
                    ActionFieldProxyType fieldType = annotationMap.get(a.getClass());
                    List<Field> fields;
                    if (fieldsData.containsKey(fieldType)) {
                        fields = fieldsData.get(fieldType);
                    } else {
                        fields = new ArrayList<Field>();
                        fieldsData.put(fieldType, fields);
                    }
                    fields.add(f);
                    break;
                }
            }
        }
    }
    
    protected void loadMethods(Class<?> actionClass){
        Map<Class<?>, ActionMethodProxyType> annotationMap = ActionMethodProxyType.getAnnotationMap();
        for(Method m : actionClass.getDeclaredMethods()){
            for(Annotation a : m.getDeclaredAnnotations()){
                if(annotationMap.containsKey(a.getClass())){
                    SortedSet<Method> methods;
                    ActionMethodProxyType methodType = annotationMap.get(a.getClass());
                    if(methodsData.containsKey(methodType)){
                        methods=methodsData.get(methodType);
                    }else if(methodComparators.containsKey(methodType)) {
                        methods = new TreeSet<Method>(methodComparators.get(methodType));
                        methodsData.put(methodType, methods);
                    }else{
                        methods = null;
                        Logger.getLogger(ActionInfoProxyImpl.class.getName()).log(Level.WARNING, "Unknow comparator for type:{0} method:{1} will be ignored.", new Object[]{methodType, m.toString()});
                    }
                    if(methods != null){
                        methods.add(m);
                        break;
                    }
                }
            }
        }
    }
}
