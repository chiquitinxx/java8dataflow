package org.chiquitinxx.funz;

public interface MaybeValue<T> extends Value<T> {
    T orElse(T elseValue);
    Throwable getError();
}
