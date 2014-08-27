package org.grooscript.concurrency;

import java.util.concurrent.Future;

/**
 * Created by jorge on 09/07/14.
 */
public class ThreadTaskResult implements TaskResult {

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
    public TaskResult then(Runnable runnable) {
        TaskResult result;
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
}
