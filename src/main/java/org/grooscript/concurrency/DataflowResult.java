package org.grooscript.concurrency;

/**
 * Created by jorge on 16/05/14.
 */
@FunctionalInterface
public interface DataflowResult<T> {
    public void whenBound(T result);
}
