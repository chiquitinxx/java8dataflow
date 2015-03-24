package org.chiquitinxx.concurrency;

import java.util.List;

/**
 * Created by jorge on 16/05/14.
 */
@FunctionalInterface
public interface AllBoundedFunction<T> {
    void allDone(List<T> values);
}
