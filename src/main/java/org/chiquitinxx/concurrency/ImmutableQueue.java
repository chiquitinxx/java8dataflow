package org.chiquitinxx.concurrency;

/**
 * Created by jorge on 15/05/14.
 */
public class ImmutableQueue<T> {

    ImmutableData<T> begin = null;
    ImmutableData<T> end = null;

    public synchronized void add(T value) {
        begin = new ImmutableData<>(value, begin);
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
