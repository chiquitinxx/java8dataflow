package org.grooscript.concurrency;

/**
 * Created by jorge on 16/05/14.
 */
@FunctionalInterface
public interface DataflowChangeResult<T> {
    public T then(T result);
}
