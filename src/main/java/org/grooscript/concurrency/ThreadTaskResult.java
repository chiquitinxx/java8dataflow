package org.grooscript.concurrency;

import java.util.concurrent.Callable;

/**
 * Created by jorge on 09/07/14.
 */
public class ThreadTaskResult implements TaskResult {

    private Thread mainThread;
    private Throwable mainException = null;

    public ThreadTaskResult(Runnable runnable) {
        mainThread = new Thread(runnable);
        mainThread.setUncaughtExceptionHandler((thread, throwable) -> {
            mainException = throwable;
        });
        mainThread.start();
    }

    @Override
    public TaskResult then(Runnable runnable) {
        TaskResult result;
        if (mainThread.isAlive()) {
            try {
                mainThread.join();
            } catch (Exception e) {
                mainException = e;
            }
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
}
