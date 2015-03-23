package org.chiquitinxx.concurrency;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jorge on 14/05/14.
 */
public abstract class DataflowPromise<T> implements Future<T> {

    volatile boolean done = false;
    volatile boolean cancelled = false;
    volatile boolean interrupt = false;

    abstract boolean notHasValue();
    protected abstract T getValue();
    protected abstract void setValue(T value);

    /**
     * Sets the value
     * @param value
     */
    public synchronized void set(T value) {
        setValue(value);
        notifyAll();
    }

    /**
     * Waits if necessary for the computation to complete, and then
     * retrieves its result.
     *
     * @return the computed result
     * @throws java.util.concurrent.CancellationException if the computation was cancelled
     * @throws java.util.concurrent.ExecutionException    if the computation threw an
     *                               exception
     * @throws InterruptedException  if the current thread was interrupted
     *                               while waiting
     */
    public T get() throws InterruptedException, ExecutionException {
        if (notHasValue() && !interrupt && !isCancelled()) {
            waitBounded();
        }
        if (interrupt) {
            throw new InterruptedException();
        }
        return getValue();
    }

    private synchronized void waitBounded()
    {
        try {
            wait();
        } catch (InterruptedException ie) {
            interrupt = true;
        }
    }

    /**
     * Attempts to cancel execution of this task.  This attempt will
     * fail if the task has already completed, has already been cancelled,
     * or could not be cancelled for some other reason. If successful,
     * and this task has not started when {@code cancel} is called,
     * this task should never run.  If the task has already started,
     * then the {@code mayInterruptIfRunning} parameter determines
     * whether the thread executing this task should be interrupted in
     * an attempt to stop the task.
     * <p>
     * <p>After this method returns, subsequent calls to {@link #isDone} will
     * always return {@code true}.  Subsequent calls to {@link #isCancelled}
     * will always return {@code true} if this method returned {@code true}.
     *
     * @param mayInterruptIfRunning {@code true} if the thread executing this
     *                              task should be interrupted; otherwise, in-progress tasks are allowed
     *                              to complete
     * @return {@code false} if the task could not be cancelled,
     * typically because it has already completed normally;
     * {@code true} otherwise
     */
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        boolean canBeCancelled = !done;
        if (mayInterruptIfRunning) {
            interrupt = true;
        }
        cancelled = true;
        done = true;
        notifyAll();
        return !canBeCancelled;
    }

    /**
     * Returns {@code true} if this task was cancelled before it completed
     * normally.
     *
     * @return {@code true} if this task was cancelled before it completed
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Returns {@code true} if this task completed.
     * <p>
     * Completion may be due to normal termination, an exception, or
     * cancellation -- in all of these cases, this method will return
     * {@code true}.
     *
     * @return {@code true} if this task completed
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Waits if necessary for at most the given time for the computation
     * to complete, and then retrieves its result, if available.
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return the computed result
     * @throws java.util.concurrent.CancellationException if the computation was cancelled
     * @throws java.util.concurrent.ExecutionException    if the computation threw an
     *                               exception
     * @throws InterruptedException  if the current thread was interrupted
     *                               while waiting
     * @throws java.util.concurrent.TimeoutException      if the wait timed out
     */
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long maxTime = unit.toMillis(timeout) + new Date().getTime();

        while (notHasValue() && !interrupt && !isCancelled()) {
            if (new Date().getTime() > maxTime) {
                throw new TimeoutException();
            }
        }
        if (interrupt) {
            throw new InterruptedException();
        }
        return getValue();
    }
}
