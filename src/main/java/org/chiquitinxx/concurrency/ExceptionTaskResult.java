package org.chiquitinxx.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jorge on 09/07/14.
 */
public class ExceptionTaskResult implements FutureResult {

    private Throwable mainException;

    public ExceptionTaskResult(Throwable throwable) {
        this.mainException = throwable;
    }

    @Override
    public FutureResult then(Runnable runnable) {
        return this;
    }

    @Override
    public void onError(ErrorResult errorResult) {
        errorResult.onException(mainException);
    }

    @Override
    public void join() { };

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    @Override
    public boolean isCancelled() {
        return true;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
