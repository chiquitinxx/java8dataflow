package org.grooscript.concurrency;

/**
 * Created by jorge on 09/07/14.
 */
public class ExceptionTaskResult implements TaskResult {

    private Throwable mainException;

    public ExceptionTaskResult(Throwable throwable) {
        this.mainException = throwable;
    }

    @Override
    public TaskResult then(Runnable runnable) {
        return this;
    }

    @Override
    public void onError(ErrorResult errorResult) {
        errorResult.onException(mainException);
    }

    @Override
    public void join() { };
}
