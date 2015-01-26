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
import java.util.List;
import java.util.ListIterator;

/**
 * Create reverse iterator for list. This will iterate items in list from last to first item in list.
 *
 * @author casper
 * @param <T> the type of elements returned by this iterator.
 */
public class ReverseIterator<T> implements Iterator<T>, Iterable<T> {

    /**
     * Internal list iterator which is used to iterate items.
     */
    protected ListIterator<T> iterator;

    /**
     * Construct new instance of reverse iterator from list given by parameter.
     *
     * @param list list which need to be iterate in reverse order.
     * @throws NullPointerException if the specified list is null.
     */
    public ReverseIterator(List<T> list) {
        this.iterator = list.listIterator(list.size());
    }

    @Override
    public boolean hasNext() {
        return iterator.hasPrevious();
    }

    @Override
    public T next() {
        return iterator.previous();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

}
