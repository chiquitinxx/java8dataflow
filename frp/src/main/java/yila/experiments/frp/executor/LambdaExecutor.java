package yila.experiments.frp.executor;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;

import static yila.experiments.frp.executor.InitialExecutor.defaultExecutor;

/**
 * Created by jorgefrancoleza on 18/10/15.
 */
public class LambdaExecutor {

    private static LambdaExecutor lambdaExecutor;
    private static final int NUMBER_THREADS = 500;

    private static LambdaExecutor getExecutor() {
        if (lambdaExecutor == null) {
            lambdaExecutor = new LambdaExecutor();
        }
        return lambdaExecutor;
    }

    public static Optional<List<Runnable>> stop() throws InterruptedException {
        List<Runnable> result = null;
        if (lambdaExecutor != null) {
            lambdaExecutor.running = false;
            lambdaExecutor.mainExecutor.shutdown();
            lambdaExecutor.elementsExecutor.shutdown();
            lambdaExecutor.mainExecutor.awaitTermination(3, TimeUnit.SECONDS);
            result = lambdaExecutor.elementsExecutor.shutdownNow();
        }
        lambdaExecutor = null;
        return Optional.ofNullable(result);
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
        mainExecutor.execute(() ->{
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
}
