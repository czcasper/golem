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
package cz.a_d.automation.testClasses.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author casper
 */
// TODO Documentation: Create javadoc on Class and public methods.
public class CloneableObject<T> implements Cloneable {

    private T value;

    public CloneableObject() {
    }

    public CloneableObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    
    protected Object clone() throws CloneNotSupportedException {
        CloneableObject<T> retValue = (CloneableObject<T>) super.clone();
        try {
            Class<? extends Object> vClass = value.getClass();
            Constructor<? extends Object> constructor = vClass.getConstructor(vClass);
            // TODO Refactoring: Find more type save way how to provide same functionality.
            retValue.value = (T) constructor.newInstance(value);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            throw new CloneNotSupportedException(ex.getLocalizedMessage());
        }
        return retValue;
    }
}
