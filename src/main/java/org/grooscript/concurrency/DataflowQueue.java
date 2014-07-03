package org.grooscript.concurrency;

/**
 * Created by jorge on 14/05/14.
 */
public class DataflowQueue<T> extends DataflowPromise<T> {

    ImmutableQueue<T> queue = new ImmutableQueue<>();

    protected void setValue(T value) {
        queue.add(value);
    }

    boolean notHasValue() {
        return queue.isEmpty();
    }

    T getValue() {
        T result = queue.remove();
        return result;
    }
}
