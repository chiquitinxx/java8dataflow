package org.grooscript.concurrency;

/**
 * Created by jorge on 09/07/14.
 */
public interface TaskResult {
    public TaskResult then(Runnable runnable);
    public void onError(ErrorResult errorResult);
    public void join();
}
