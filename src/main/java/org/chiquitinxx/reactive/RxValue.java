package org.chiquitinxx.reactive;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by jorgefrancoleza on 5/12/16.
 */
public class RxValue<T> implements ReactiveValue<T> {

    private final CompletableFuture<T> completableFuture;
    private boolean done = false;
    private boolean onError = false;

    public RxValue(Supplier<T> supplier) {
        this.completableFuture = CompletableFuture.supplyAsync(supplier);
    }

    @Override
    public ReactiveValue<T> onDone(Consumer<T> consumer) {
        executeConsumerIfNotDoneBefore(consumer);
        return this;
    }

    @Override
    public ReactiveValue<T> onError(Function<Throwable, T> function) {
        setOnErrorIfNotDoneBefore(function);
        return this;
    }

    private synchronized void executeConsumerIfNotDoneBefore(Consumer<T> consumer) {
        if (!done) {
            this.completableFuture.thenAcceptAsync(consumer);
            done = true;
        } else {
            throw new RuntimeException("onDone method already selected, can't do more than once");
        }
    }

    private synchronized void setOnErrorIfNotDoneBefore(Function<Throwable, T> function) {
        if (!onError) {
            completableFuture.exceptionally(function::apply);
            onError = true;
        } else {
            throw new RuntimeException("onError method already selected, can't do more than once");
        }
    }
}
