package org.grooscript.concurrency;

/**
 * Created by jorge on 14/05/14.
 */
public class DataflowQueue<T> extends DataflowPromise<T> {

    private ImmutableQueue<T> queue = new ImmutableQueue<>();

    protected void setValue(T value) {
        queue.add(value);
    }

    boolean notHasValue() {
        return queue.isEmpty();
    }

    protected T getValue() {
        T result = queue.remove();
        return result;
    }

    public int size() {
        if (queue.isEmpty()) {
            return 0;
        } else {
            int number = 1;
            ImmutableData<T> actual = queue.begin;
            while (actual.getNext() != null) {
                number ++;
                actual = actual.getNext();
            }
            return number;
        }
    }
}
