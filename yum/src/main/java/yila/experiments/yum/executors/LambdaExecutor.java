package yila.experiments.yum.executors;

import java.util.Queue;
import java.util.concurrent.*;

public class LambdaExecutor {

    private static LambdaExecutor lambdaExecutor;
    private static final int NUMBER_THREADS = 500;

    private static LambdaExecutor getExecutor() {
        if (lambdaExecutor == null) {
            lambdaExecutor = new LambdaExecutor();
        }
        return lambdaExecutor;
    }

    public static void executeAsync(Runnable runnable) {
        getExecutor().addToQueue(runnable);
    }

    private Queue<Runnable> queue;
    private ExecutorService mainExecutor;
    private ExecutorService elementsExecutor;
    private volatile boolean running = true;

    private LambdaExecutor() {
        mainExecutor = defaultExecutor();
        //Limit number of threads can be running or can get: java.lang.OutOfMemoryError: unable to create new native thread
        elementsExecutor = Executors.newFixedThreadPool(NUMBER_THREADS);
        queue = new ConcurrentLinkedQueue<>();
        mainExecutor.execute(() -> {
            while (running) {
                final Runnable runnable = queue.poll();
                if (runnable != null) {
                    elementsExecutor.submit(runnable);
                }
            }
        });
    }

    private void addToQueue(Runnable runnable) {
        queue.add(runnable);
    }

    private ExecutorService defaultExecutor() {
        return new ForkJoinPool();
    }
}
