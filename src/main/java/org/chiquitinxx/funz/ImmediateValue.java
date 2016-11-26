package org.chiquitinxx.funz;

import java.util.function.Supplier;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
public class ImmediateValue<T> implements Value<T> {

    private T result;

    public ImmediateValue(Supplier<T> supplier) {
        this.result = supplier.get();
    }

    @Override
    public T get() {
        return result;
    }
}
