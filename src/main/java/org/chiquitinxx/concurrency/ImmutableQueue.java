package org.chiquitinxx.concurrency;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by jorge on 15/05/14.
 */
public class ImmutableQueue<T> {

    private LinkedList<T> linkedList = new LinkedList<>();

    public synchronized ImmutableQueue<T> add(T value) {
        ImmutableQueue<T> result = new ImmutableQueue<T>();
        linkedList.add(value);
        result.addItems(linkedList);
        return result;
    }

    public synchronized T remove() {
        return linkedList.removeFirst();
    }

    public boolean isEmpty() {
        return linkedList.size() < 1;
    }

    public int getSize() {
        return linkedList.size();
    }

    protected void addItems(Collection items) {
        linkedList.addAll(items);
    }
}
