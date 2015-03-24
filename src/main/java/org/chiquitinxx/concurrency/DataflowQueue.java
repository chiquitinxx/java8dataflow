package org.chiquitinxx.concurrency;

import java.util.LinkedList;

/**
 * Created by jorge on 14/05/14.
 */
public class DataflowQueue<T> extends DataflowPromise<T> {

    private LinkedList<T> linkedList = new LinkedList<>();

    protected synchronized void setValue(T value) {
        linkedList.add(value);
    }

    boolean notHasValue() {
        return linkedList.isEmpty();
    }

    protected synchronized T getValue() {
        return linkedList.removeFirst();
    }

    public int getSize() {
        return linkedList.size();
    }
}
