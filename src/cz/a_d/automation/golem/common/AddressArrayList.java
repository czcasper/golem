/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.common;

import cz.a_d.automation.golem.common.iterators.ReverseIterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @param <T>
 * @author maslu02
 */
public class AddressArrayList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 1L;

    /**
     *
     * @param initialCapacity
     */
    public AddressArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     *
     */
    public AddressArrayList() {
        super();
    }

    /**
     *
     * @param c
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
