/*
 */
package cz.a_d.automation.golem.common;

import cz.a_d.automation.golem.common.iterators.ReverseIterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Extension of Arraylist which is comparing stored objects by using address of object not by using method equal. This allow to store
 * multiple instances of same object and safely get index of instance which you currently have. This feature is used by Golem iterators for
 * keeping and safely manipulating actions in stream.
 *
 * @author casper
 * @param <T> the type of elements in this list.
 */
public class AddressArrayList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity is negative
     */
    public AddressArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public AddressArrayList() {
        super();
    }

    /**
     * Constructs a list containing the elements of the specified collection, in the order they are returned by the collection's iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public AddressArrayList(Collection<? extends T> c) {
        super(c);
    }

    @Override
    public int indexOf(Object o) {
        int retValue = -1, tmpIndex = 0;
        for (T tmp : this) {
            if (tmp == o) {
                retValue = tmpIndex;
                break;
            }
            tmpIndex++;
        }

        return retValue;
    }

    @Override
    public int lastIndexOf(Object o) {
        int retValue = -1, tmpIndex = size();
        for (T tmp : new ReverseIterator<>(this)) {
            tmpIndex--;
            if (tmp == o) {
                retValue = tmpIndex;
                break;
            }
        }
        return retValue;
    }
}
