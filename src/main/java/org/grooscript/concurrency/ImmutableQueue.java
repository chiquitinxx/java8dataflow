package org.grooscript.concurrency;

/**
 * Created by jorge on 15/05/14.
 */
public class ImmutableQueue<T> {

    volatile ImmutableData<T> begin = null;
    volatile ImmutableData<T> end = null;

    public void add(T value) {
        ImmutableData<T> newData = new ImmutableData<>(value, begin);
        begin = newData;
        if (end == null) {
            end = begin;
        }
    }

    public synchronized T remove() {
        if (!isEmpty()) {
            T last = end.getValue();
            if (begin == end) {
                begin = null;
                end = null;
            } else {
                end = findNextIsEnd(begin, end);
            }
            return last;
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return begin == null;
    }

    private ImmutableData<T> findNextIsEnd(ImmutableData<T> begin, ImmutableData<T> end) {
        ImmutableData<T> actual = begin;
        while (actual.getNext() != end) {
            actual = actual.getNext();
        }
        return actual;
    }
}
