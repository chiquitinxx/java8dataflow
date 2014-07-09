package org.grooscript.concurrency;

import java.util.concurrent.Callable;

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
}
