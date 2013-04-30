/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.spools.actions;

import com.ca.automation.golem.annotations.MethodAnnotationFieldComparator;
import com.ca.automation.golem.annotations.RunAction;
import com.ca.automation.golem.annotations.fields.RunConnection;
import com.ca.automation.golem.annotations.fields.RunContext;
import com.ca.automation.golem.annotations.fields.RunParameter;
import com.ca.automation.golem.annotations.methods.Init;
import com.ca.automation.golem.annotations.methods.Run;
import com.ca.automation.golem.annotations.methods.Validate;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

/**
 *
 * @author maslu02
 */
@Singleton
@LocalBean
public class ActionReflectionContainer {

    private static ActionReflectionContainer defaultInstance = null;
    private Map<Class<?>, ProxyContainer> storageMap;
    private MethodAnnotationFieldComparator<Init> initSort = null;
    private MethodAnnotationFieldComparator<Run> runSort = null;
    private MethodAnnotationFieldComparator<Validate> validateSort = null;

    /**
     * This is method for using this class in JDK aplication without JEE
     * container.It will creates static instance of action container and
     * provides same initialization and similar sharing policy like in JEE.
     *
     * @return static instance of this
     * @class ActionReflectionContainer
     */
    public static ActionReflectionContainer getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ActionReflectionContainer();
            defaultInstance.initMap();
        }
        return defaultInstance;
    }

    /**
     *
     */
    @PostConstruct
    public void initMap() {
        try {
            storageMap = new LinkedHashMap<Class<?>, ProxyContainer>(16, 0.75f, true);

            initSort = new MethodAnnotationFieldComparator<Init>(Init.class, "order");
            runSort = new MethodAnnotationFieldComparator<Run>(Run.class, "order");
            validateSort = new MethodAnnotationFieldComparator<Validate>(Validate.class, "order");

        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ActionReflectionContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param action
     * @return
     */
    public List<Field> getFields(Object action) {
        List<Field> retValue = null;
        if (isAction(action)) {
            retValue = storageMap.get(action.getClass()).getFields();
        }
        return retValue;
    }

    /**
     *
     * @param action
     * @param name
     * @return
     */
    public Field getFieldByName(Object action, String name) {
        return getFieldByName(action, name, false);
    }

    /**
     *
     * @param action
     * @param name
     * @param force
     * @return
     */
    protected Field getFieldByName(Object action, String name, boolean force) {
        Field retValue = null;
        if (isAction(action)) {
            Map<String, Field> fieldsNameMap;
            if (force) {
                fieldsNameMap = storageMap.get(action.getClass()).getFreshFieldsNameMap();
            } else {
                fieldsNameMap = storageMap.get(action.getClass()).getFieldsNameMap();
            }
            if (fieldsNameMap.containsKey(name)) {
                retValue = fieldsNameMap.get(name);
            }
        }
        return retValue;
    }

    /**
     *
     * @param action
     * @return
     */
    public List<Field> getConnections(Object action) {
        List<Field> retValue = null;
        if (isAction(action)) {
            retValue = storageMap.get(action.getClass()).getConnections();
        }
        return retValue;
    }

    /**
     *
     * @param action
     * @return
     */
    public List<Field> getRetValues(Object action) {
        List<Field> retValue = null;
        if (isAction(action)) {
            retValue = storageMap.get(action.getClass()).getRetValues();
        }
        return retValue;
    }

    /**
     *
     * @param action
     * @return
     */
    public List<Field> getContexts(Object action) {
        List<Field> retValue = null;
        if (isAction(action)) {
            retValue = storageMap.get(action.getClass()).getContext();
        }
        return retValue;
    }

    /**
     *
     * @param action
     * @return
     */
    public List<Method> getInits(Object action) {
        List<Method> retValue = null;
        if (isAction(action)) {
            retValue = storageMap.get(action.getClass()).getInits();
        }
        return retValue;
    }

    /**
     *
     * @param action
     * @return
     */
    public List<Method> getRuns(Object action) {
        List<Method> retValue = null;
        if (isAction(action)) {
            retValue = storageMap.get(action.getClass()).getRuns();
        }
        return retValue;
    }

    /**
     *
     * @param action
     * @return
     */
    public List<Method> getValidates(Object action) {
        List<Method> retValue = null;
        if (isAction(action)) {
            retValue = storageMap.get(action.getClass()).getValidates();
        }
        return retValue;
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean isAction(Object obj) {
        boolean retValue = false;
        if (obj != null) {
            Class<? extends Object> actionCl = obj.getClass();
            if (storageMap.containsKey(actionCl)) {
                retValue = true;
            } else {
                ProxyContainer pc = new ProxyContainer();
                if (processObjectClass(actionCl, pc)) {

                    //Sorting methods by value in annotation field order
                    Collections.sort(pc.getInits(), initSort);
                    Collections.sort(pc.getRuns(), runSort);
                    Collections.sort(pc.getValidates(), validateSort);

                    storageMap.put(actionCl, pc);
                    retValue = true;
                }
            }
        }

        return retValue;
    }

    private void loadObjectFields(Class<?> cl, ProxyContainer pc) {
        List<Field> fields = new ArrayList<Field>();
        List<Field> retValues = new ArrayList<Field>();
        List<Field> context = new ArrayList<Field>();
        List<Field> connections = new ArrayList<Field>();

        for (Field field : cl.getDeclaredFields()) {
            if (field.isAnnotationPresent(RunParameter.class)) {
                field.setAccessible(true);
                fields.add(field);

                RunParameter param = field.getAnnotation(RunParameter.class);
                if (param.retValue()) {
                    retValues.add(field);
                }
                continue;
            }

            if (field.isAnnotationPresent(RunContext.class)) {
                field.setAccessible(true);
                context.add(field);
                continue;
            }

            if (field.isAnnotationPresent(RunConnection.class)) {
                field.setAccessible(true);
                connections.add(field);
                continue;
            }
        }

        pc.addFields(fields);
        pc.addRetValues(retValues);
        pc.addContext(context);
        pc.addConnections(connections);
    }

    private void loadObjectMethods(Class<?> cl, ProxyContainer pc) {
        List<Method> initMethods = new ArrayList<Method>();
        List<Method> runMethods = new ArrayList<Method>();
        List<Method> validateMethods = new ArrayList<Method>();

        for (Method method : cl.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Init.class)) {
                method.setAccessible(true);

                initMethods.add(method);
            } else if (method.isAnnotationPresent(Run.class)) {
                method.setAccessible(true);

                runMethods.add(method);
            } else if (method.isAnnotationPresent(Validate.class)) {
                method.setAccessible(true);

                validateMethods.add(method);
            }
        }

        pc.addInits(initMethods);
        pc.addRuns(runMethods);
        pc.addValidates(validateMethods);
    }

    private boolean processObjectClass(Class<?> cl, ProxyContainer pc) {
        boolean retValue = false;
        Class<?> sCl;

        do {
            sCl = cl.getSuperclass();
            if (cl.isAnnotationPresent(RunAction.class)) {
                if (storageMap.containsKey(cl)) {
                    pc.addProxy(storageMap.get(cl));
                    break;
                } else {
                    loadObjectFields(cl, pc);
                    loadObjectMethods(cl, pc);
                }
            }
            cl = sCl;
        } while ((sCl != null) && (sCl.getName().compareTo("java.lang.Object") != 0));

        if (pc.hasRuns()) {
            retValue = true;
        }
        return retValue;
    }

    private class ProxyContainer {

        private List<Field> fields;
        private Map<String, Field> fieldsNameMap = null;
        private List<Field> connections;
        private List<Field> retValues;
        private List<Field> context;
        private List<Method> inits;
        private List<Method> runs;
        private List<Method> validates;

        public void addProxy(ProxyContainer pc) {
            if (pc != null) {
                addFields(pc.fields);
                if (pc.fieldsNameMap != null) {
                    if (fieldsNameMap == null) {
                        fieldsNameMap = getNamedMap(fields);
                    }
                    fieldsNameMap.putAll(pc.fieldsNameMap);
                }
                addConnections(pc.connections);
                addRetValues(pc.retValues);
                addContext(pc.context);
                addInits(pc.inits);
                addRuns(pc.runs);
                addValidates(pc.validates);
            }
        }

        public List<Field> getConnections() {
            return connections;
        }

        public void setConnections(List<Field> connections) {
            this.connections = connections;
        }

        public void addConnections(List<Field> connections) {
            this.connections = addList(this.connections, connections);;
        }

        public List<Field> getContext() {
            return context;
        }

        public void setContext(List<Field> context) {
            this.context = context;
        }

        public void addContext(List<Field> context) {
            this.context = addList(this.context, context);
        }

        public List<Field> getRetValues() {
            return retValues;
        }

        public void setRetValues(List<Field> retValues) {
            this.retValues = retValues;
        }

        public void addRetValues(List<Field> retValues) {
            this.retValues = addList(this.retValues, retValues);
        }

        public List<Field> getFields() {
            return fields;
        }

        public void setFields(List<Field> fields) {
            this.fields = fields;
        }

        public void addFields(List<Field> fields) {
            this.fields = addList(this.fields, fields);
        }

        public Map<String, Field> getFreshFieldsNameMap() {
            fieldsNameMap = null;
            return getFieldsNameMap();
        }

        public Map<String, Field> getFieldsNameMap() {
            if (fieldsNameMap == null) {
                fieldsNameMap = getNamedMap(fields);
            }
            return fieldsNameMap;
        }

        public List<Method> getInits() {
            return inits;
        }

        public void setInits(List<Method> inits) {
            this.inits = inits;
        }

        public void addInits(List<Method> inits) {
            this.inits = addList(this.inits, inits);
        }

        public List<Method> getRuns() {
            return runs;
        }

        public void setRuns(List<Method> runs) {
            this.runs = runs;
        }

        public void addRuns(List<Method> runs) {
            this.runs = addList(this.runs, runs);
        }

        public boolean hasRuns() {
            return ((runs != null) && (!runs.isEmpty()));
        }

        public List<Method> getValidates() {
            return validates;
        }

        public void setValidates(List<Method> validates) {
            this.validates = validates;
        }

        public void addValidates(List<Method> validates) {
            this.validates = addList(this.validates, validates);
        }

        private <T> List<T> addList(List<T> member, List<T> newList) {
            List<T> retvalue = null;
            if (member == null) {
                retvalue = newList;
            } else if (newList != null) {
                member.addAll(newList);
                retvalue = member;
            }
            return retvalue;
        }

        private Map<String, Field> getNamedMap(List<Field> fields) {
            Map<String, Field> retValue = null;
            if (fields != null && !fields.isEmpty()) {
                retValue = new HashMap<String, Field>();
                for (Field f : fields) {
                    retValue.put(f.getName(), f);
                }
            }
            return retValue;
        }
    }
}
