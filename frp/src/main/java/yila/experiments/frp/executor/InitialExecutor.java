package yila.experiments.frp.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Created by jorgefrancoleza on 18/10/15.
 */
public class InitialExecutor {

    protected ExecutorService currentExecutor;
    protected volatile boolean running = true;

    protected static ExecutorService defaultExecutor() {
        return new ForkJoinPool();
    }

    protected ExecutorService assignExecutor(ExecutorService executorService) {
        this.currentExecutor = executorService;
        return this.currentExecutor;
    }

    protected void stop() throws InterruptedException {
        running = false;
        if (currentExecutor != null) {
            currentExecutor.shutdown();
            currentExecutor.awaitTermination(2, TimeUnit.SECONDS);
        }
    }
}
