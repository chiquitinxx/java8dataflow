package org.chiquitinxx.funz;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
public class ImmediateValue<T> implements Value<T> {

    private final T value;

    public ImmediateValue(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
