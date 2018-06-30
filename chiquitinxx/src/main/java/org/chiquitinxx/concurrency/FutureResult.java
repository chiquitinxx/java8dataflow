package org.chiquitinxx.concurrency;

import java.util.concurrent.Future;

/**
 * User: jorgefrancoleza
 * Date: 15/09/14
 */
public interface FutureResult<V> extends Future<V> {
    FutureResult<V> then(Runnable runnable);
    void onError(ErrorResult errorResult);
    void join();
}
