package org.grooscript.concurrency;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by jorge on 15/05/14.
 */
public class Task {

    static TaskResult task(Runnable runnable) {
        return new ThreadTaskResult(runnable);
    }

    static Future task(Callable callable) throws Exception {
        DataflowVariable dataflowVariable = new DataflowVariable<>();
        dataflowVariable.set(callable.call());
        return dataflowVariable;
    }

    static void whenAllBound(AllBoundedFunction whenAllBounded, Future... futures)
            throws InterruptedException, ExecutionException {
        Stream<Future> stream = Arrays.stream(futures).parallel();

        whenAllBounded.allDone(stream.map(e -> {
            try {
                return e.get();
            } catch (Exception ee) {

            }
            return null;
        }).toArray());
    }
}
