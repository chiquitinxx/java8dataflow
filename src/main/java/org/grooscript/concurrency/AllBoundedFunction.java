package org.grooscript.concurrency;

import java.util.stream.Stream;

/**
 * Created by jorge on 16/05/14.
 */
@FunctionalInterface
public interface AllBoundedFunction {
    public void allDone(Stream values);
}
