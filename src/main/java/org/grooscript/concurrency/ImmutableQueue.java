package org.grooscript.concurrency;

/**
 * Created by jorge on 15/05/14.
 */
public class ImmutableQueue<T> {

    private ImmutableData<T> begin = null;

    public synchronized void add(T value) {
        ImmutableData<T> newData = new ImmutableData<>(value, null);
        begin = reconstructQueueAndGetFirst(begin, newData);
    }

    public synchronized T remove() {
        if (!isEmpty()) {
            T last = removeFirst();
            return last;
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return begin == null;
    }

    private ImmutableData<T> reconstructQueueAndGetFirst(ImmutableData<T> current, ImmutableData<T> last) {
        if (current == null) {
            return last;
        } else {
            return new ImmutableData<T>(current.getValue(), reconstructQueueAndGetFirst(current.getNext(), last));
        }
    }

    private T removeFirst() {
        T beginValue = begin.getValue();
        begin = begin.getNext();
        return beginValue;
    }
}
