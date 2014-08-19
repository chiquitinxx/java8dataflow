package org.grooscript.concurrency;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by jorge on 15/05/14.
 */
public class Task {

    static TaskResult task(Runnable runnable) {
        return new ThreadTaskResult(runnable);
    }

    static <T> Future task(Callable<T> callable) throws Exception {
        DataflowVariable<T> dataflowVariable = new DataflowVariable<>();
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
                ee.printStackTrace();
            }
            return null;
        }).toArray());
    }
}