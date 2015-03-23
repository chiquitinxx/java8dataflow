package org.chiquitinxx.concurrency;

import java.util.ArrayList;
import java.util.List;

import static org.chiquitinxx.concurrency.Task.*;

/**
 * Created by jorge on 14/05/14.
 */
public class DataflowVariable<T> extends DataflowPromise<T> implements FutureResult<T> {

    private T value = null;
    private volatile boolean hasSettedValue = false;
    private DataflowResult<T> bounded;
    private List<Runnable> thenRunnableFunctions = new ArrayList<>();

    protected void setValue(T value) {
        if (!hasSettedValue) {
            hasSettedValue = true;
            executeBoundedIfExists(value);
            executeThenFunctions();
            this.value = value;
            done = true;
        }
    }

    private void executeBoundedIfExists(T value) {
        if (bounded != null) {
            try {
                bounded.whenBound(value);
            } catch (Exception e) {
                System.out.println("Error in bounded function: "+e.getMessage());
            }
        }
    }

    boolean notHasValue() {
        return hasSettedValue == false;
    }

    protected T getValue() {
        return value;
    }

    /**
     * @param whenBound
     */
    public void whenBound(DataflowResult<T> whenBound) {
        bounded = whenBound;
    }


    public DataflowVariable<T> then(DataflowChangeResult<T> thenFunction) {
        DataflowVariable<T> result;
        try {
            result = task(() -> thenFunction.then(this.get()));

        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public FutureResult<T> then(Runnable runnable) {
        thenRunnableFunctions.add(runnable);
        return this;
    }

    private void executeThenFunctions() {
        for (Runnable function: thenRunnableFunctions) {
            function.run();
        }
    }

    @Override
    public void onError(ErrorResult errorResult) {

    }

    @Override
    public void join() {
        try {
            this.get();
        } catch (Exception e) {
            //
        }
    }
}
