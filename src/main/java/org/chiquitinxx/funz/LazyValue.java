package org.chiquitinxx.funz;

import java.util.function.Supplier;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
public class LazyValue<T> implements Value<T> {

    private final Supplier<T> supplier;

    /**
     * Supplier cant return null
     * @param supplier
     */
    public LazyValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        T value = this.supplier.get();
        if (value == null)
            throw new SupplierReturnsNullException();
        return this.supplier.get();
    }
}
