package org.grooscript.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jorge on 09/07/14.
 */
public class ThreadTaskResult implements FutureResult {

    private Thread mainThread;
    private Throwable mainException = null;
    Future result;

    public ThreadTaskResult(Runnable runnable) {
        mainThread = new Thread(runnable);
        mainThread.setUncaughtExceptionHandler((thread, throwable) -> {
            mainException = throwable;
        });
        mainThread.start();
        result = Task.task(() -> mainThread);
    }

    @Override
    public FutureResult then(Runnable runnable) {
        FutureResult result;
        if (mainThread.isAlive()) {
            waitForEndTask();
        }
        if (mainException != null) {
            result = new ExceptionTaskResult(mainException);
        } else {
            result = new ThreadTaskResult(runnable);
        }
        return result;
    }

    @Override
    public void onError(ErrorResult errorResult) {
        if (mainException != null) {
            errorResult.onException(mainException);
        }
    }

    @Override
    public void join() {
        if (mainThread != null && mainThread.isAlive()) {
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForEndTask() {
        try {
            result.get();
        } catch (Exception e) {
            System.out.println("Error waiting: "+e);
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return result.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return result.isCancelled();
    }

    @Override
    public boolean isDone() {
        return result.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return result.get();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return result.get(timeout, unit);
    }
}
