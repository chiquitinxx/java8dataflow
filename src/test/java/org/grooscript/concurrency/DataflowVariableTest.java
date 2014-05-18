package org.grooscript.concurrency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.ExecutionException;

import static org.grooscript.concurrency.Task.task;
import static org.junit.Assert.assertEquals;

/**
 * Created by jorge on 15/05/14.
 */
@RunWith(JUnit4.class)
public class DataflowVariableTest {

    String text;

    @Test
    public void testMultipleVariables() throws InterruptedException, ExecutionException {
        DataflowVariable<Integer> initialDistance = new DataflowVariable<Integer>();
        DataflowVariable<Integer> acceleration = new DataflowVariable<Integer>();
        DataflowVariable<Integer> time = new DataflowVariable<Integer>();
        task(() -> {
            initialDistance.set(100);
            acceleration.set(2);
            time.set(10);
        });

        int result = initialDistance.get() + acceleration.get() / 2 * (time.get() * time.get());
        assertEquals(result, 200);
    }

    @Test
    public void testWhenBound() throws InterruptedException {
        DataflowVariable<String> dv = new DataflowVariable<String>();
        dv.whenBound((newValue) -> {
            System.out.println("New value is:"+newValue);
            text = "New value is:" + newValue;
        });
        task(() -> dv.set("VALUE"));
        Thread.sleep(50);
        assertEquals("New value is:VALUE", text);
    }

    @Test
    public void testThen() throws InterruptedException {
        DataflowVariable<String> dv = new DataflowVariable<String>();
        dv.then((value) -> value.toUpperCase()).
        then((value) -> value + value).
        then((value) -> {
            text = "New value is:" + value;
            return value;
        });

        task(() -> dv.set("value"));
        Thread.sleep(50);
        assertEquals("New value is:VALUEVALUE", text);
    }
}
