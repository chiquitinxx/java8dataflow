package org.chiquitinxx.funz;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
public class AsyncImmediateValue<T> implements MaybeValue<T> {

    private final CompletableFuture<T> future;
    private Throwable throwable = null;

    public AsyncImmediateValue(Supplier<T> supplier) {
        future = CompletableFuture.supplyAsync(supplier);
    }

    @Override
    public T get() {
        T result = getValue(null);
        if (result == null)
            throw new NoSuchElementException();
        else return getValue(null);
    }

    @Override
    public T orElse(T elseValue) {
        return getValue(elseValue);
    }

    @Override
    public Throwable getError() {
        return this.throwable;
    }

    private T getValue(T failValue) {
        try {
            return future.get();
        } catch (ExecutionException e) {
            this.throwable = e.getCause();
        } catch (InterruptedException i) {
            this.throwable = i;
        }
        return failValue;
    }
}
