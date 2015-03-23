package org.chiquitinxx.concurrency;

/**
 * Created by jorge on 14/05/14.
 */
public class DataflowQueue<T> extends DataflowPromise<T> {

    private ImmutableQueue<T> queue = new ImmutableQueue<>();

    protected void setValue(T value) {
        queue = queue.add(value);
    }

    boolean notHasValue() {
        return queue.isEmpty();
    }

    protected T getValue() {
        T result = queue.remove();
        return result;
    }

    public int getSize() {
        return queue.getSize();
    }
}
