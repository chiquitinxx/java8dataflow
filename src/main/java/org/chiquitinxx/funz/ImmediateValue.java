package org.chiquitinxx.funz;

import java.io.Serializable;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
public class ImmediateValue<T> implements Value<T>, Serializable {

    private final T value;

    /**
     * Value cant be null
     * @param value
     */
    public ImmediateValue(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        if (value == null)
            throw new SupplierReturnsNullException();
        return value;
    }
}
