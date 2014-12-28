/*
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
