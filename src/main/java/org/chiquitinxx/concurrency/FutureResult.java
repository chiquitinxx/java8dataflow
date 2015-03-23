package org.chiquitinxx.concurrency;

import java.util.concurrent.Future;

/**
 * User: jorgefrancoleza
 * Date: 15/09/14
 */
public interface FutureResult<V> extends Future<V> {
    public FutureResult<V> then(Runnable runnable);
    public void onError(ErrorResult errorResult);
    public void join();
}
