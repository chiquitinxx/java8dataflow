package org.chiquitinxx.reactive;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by jorgefrancoleza on 5/12/16.
 */
public interface ReactiveValue<T> {

    ReactiveValue<T> onDone(Consumer<T> consumer);
    ReactiveValue<T> onError(Function<Throwable, T> function);

    //static <U, V, W> ReactiveValue<W> join(ReactiveValue<U> first, ReactiveValue<V> second) {
    //    return new RxValue<>();
    //}
}
