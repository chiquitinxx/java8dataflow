package org.grooscript.concurrency;

/**
 * Created by jorge on 15/05/14.
 */
public final class ImmutableData<T> {
    private final T value;
    private final ImmutableData<T> next;

    ImmutableData(T finalValue, ImmutableData<T> finalNext) {
        value = finalValue;
        next = finalNext;
    }

    public T getValue() {
        return value;
    }

    public ImmutableData<T> getNext() {
        return next;
    }
}
