package org.chiquitinxx.funz;

import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
public class AsyncImmediateValue<T> implements MaybeValue<T>, Serializable {

    private transient final CompletableFuture<T> future;
    private T value = null;
    private Throwable error = null;

    public AsyncImmediateValue(Supplier<T> supplier) {
        future = CompletableFuture.supplyAsync(supplier);
    }

    @Override
    public T get() {
        T result = getValue(null);
        if (result == null)
            throw new NoSuchElementException();
        else return result;
    }

    @Override
    public T orElse(T elseValue) {
        return getValue(elseValue);
    }

    @Override
    public Throwable getError() {
        return this.error;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof AsyncImmediateValue) {
            AsyncImmediateValue other = (AsyncImmediateValue)o;
            return equalErrors(this.error, other.error) &&
                    (this.value != null ? this.value.equals(other.value) : other.value == null);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 107;
    }

    private boolean equalErrors(Throwable one, Throwable two) {
        if (one == null && two == null) {
            return true;
        } else {
            return one != null && two != null && Arrays.deepEquals(one.getStackTrace(), two.getStackTrace());
        }
    }

    private synchronized T getValue(T failValue) {
        try {
            if (value == null && error == null) {
                value = future.get();
            }
            return value == null ? failValue : value;
        } catch (ExecutionException e) {
            this.error = e.getCause();
        } catch (InterruptedException i) {
            this.error = i;
        }
        return failValue;
    }
}
