package org.chiquitinxx.concurrency;

/**
 * Created by jorge on 16/05/14.
 */
@FunctionalInterface
public interface ErrorResult {
    public void onException(Throwable throwable);
}