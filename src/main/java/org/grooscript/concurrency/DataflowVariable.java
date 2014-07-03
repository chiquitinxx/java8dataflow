package org.grooscript.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by jorge on 14/05/14.
 */
public class DataflowVariable<T> extends DataflowPromise<T> {

    private T value = null;
    private volatile boolean hasSettedValue = false;
    private DataflowResult<T> bounded;
    private List<DataflowChangeResult<T>> thenFunctions = new ArrayList<DataflowChangeResult<T>>();

    protected void setValue(T value) {
        if (hasSettedValue == false) {
            executeBoundedIfExists(value);
            this.value = executeThenFunctions(value);
            done = true;
            hasSettedValue = true;
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

    T getValue() {
        return value;
    }

    /**
     * @param whenBound
     */
    public void whenBound(DataflowResult<T> whenBound) {
        bounded = whenBound;
    }

    DataflowVariable<T> then(DataflowChangeResult<T> thenFunction) {
        addThenFunction(thenFunction);
        return this;
    }

    private void addThenFunction(DataflowChangeResult<T> thenFunction) {
        thenFunctions.add(thenFunction);
    }

    private T executeThenFunctions(T value) {
        for (DataflowChangeResult<T> function: thenFunctions) {
            value = function.then(value);
        }
        return value;
    }
}
