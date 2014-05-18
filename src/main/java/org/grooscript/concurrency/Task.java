package org.grooscript.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * Created by jorge on 15/05/14.
 */
public class Task {
    static void task(Runnable runnable) throws InterruptedException {
        new Thread(runnable).start();
    }

    static Future task(Callable callable) throws Exception {
        DataflowVariable dataflowVariable = new DataflowVariable<>();
        dataflowVariable.set(callable.call());
        return dataflowVariable;
    }

    static Future whenAllBound(AllBoundedFunction whenAllBounded, Future... futures) throws Exception {
        Future<Object[]> future = task(() -> {
            Object[] values = new Object[futures.length];
            int i;
            for (i = 0; i < futures.length; i++) {
                values[i] = futures[i].get();
            }
            return values;
        });
        whenAllBounded.allDone(future.get());
        //dataflowVariable.whenBound((values) -> whenAllBounded.allDone(values));
    }
}
