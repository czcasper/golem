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
package cz.a_d.automation.golem.common.iterators;

import java.util.Iterator;

/**
 * Iterator implementation which allows to change collection which is used for iteration. This implementation is necessary to allow
 * controlling flow of action in Golem. This iterator is used to iterate actions in stream and allows manager change stream or change next
 * item in list which will be returned.
 *
 * @author casper
 * @param <T> the type of elements returned by this iterator.
 */
public class ResetableIterator<T> implements Iterator<T> {

    /**
     * Internal iterator used to provide data and functionality for implementation of methods requested by Iterator interface.
     */
    protected Iterator<T> it;

    /**
     * Create new instance of Resettable iterator from existing initialized iterator.
     *
     * @param it iterator which will be used to iterate data in this created instance.
     * @throws NullPointerException if the specified iterator is null.
     */
    public ResetableIterator(Iterator<T> it) {
        if (it == null) {
            throw new NullPointerException("Internal iterator is null");
        }
        this.it = it;
    }

    /**
     * Method allow change internal iterator instance. Instance is used for providing data in methods requested by iterator. This allows to
     * change data during iteration without losing validity of iterator.
     *
     * @param it new internal iterator for instance, must be diferent from null
     * @throws NullPointerException if the specified iterator is null.
     */
    public void setIt(Iterator<T> it) {
        if (it == null) {
            throw new IllegalStateException("Internal iterator iterator can't be initialized by null value");
        }
        this.it = it;
    }

    /**
     * Getter to allow access currently used internal iterator instance.
     *
     * @return instance of internal iterator. Never return null value.
     */
    public Iterator<T> getIt() {
        return it;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        return it.next();
    }

    @Override
    public void remove() {
        it.remove();
    }

}
