package org.chiquitinxx.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by jorge on 15/05/14.
 */
public class Task {

    static FutureResult task(Runnable runnable) {
        return new ThreadTaskResult(runnable);
    }

    static <T> DataflowVariable<T> task(Callable<T> callable) {
        DataflowVariable<T> dataflowVariable = new DataflowVariable<>();
        Thread thread = new Thread(() -> dataflowVariable.set(runCallable(callable)));
        thread.start();
        return dataflowVariable;
    }

    static <T> void whenAllBound(AllBoundedFunction<T> whenAllBounded, List<Future> futures) {
        whenAllBoundExecution(whenAllBounded, futures.stream().parallel());
    }

    static <T> void whenAllBound(AllBoundedFunction<T> whenAllBounded, Future... futures) {
        whenAllBoundExecution(whenAllBounded, Arrays.stream(futures).parallel());
    }

    private static void whenAllBoundExecution(AllBoundedFunction whenAllBounded, Stream<Future> stream) {
        whenAllBounded.allDone(stream.map(e -> {
            try {
                return e.get();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return null;
        }).toArray());
    }

    private static <T> T runCallable(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
