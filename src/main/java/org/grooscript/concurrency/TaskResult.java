package org.grooscript.concurrency;

import java.util.concurrent.Callable;

/**
 * Created by jorge on 09/07/14.
 */
public interface TaskResult {
    public TaskResult then(Runnable runnable);
    public void onError(ErrorResult errorResult);
}
