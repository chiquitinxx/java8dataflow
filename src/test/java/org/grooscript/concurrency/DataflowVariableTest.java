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

    volatile String text;

    @Test
    public void testMultipleVariables() throws InterruptedException, ExecutionException {
        DataflowVariable<Integer> initialDistance = new DataflowVariable<>();
        DataflowVariable<Integer> acceleration = new DataflowVariable<>();
        DataflowVariable<Integer> time = new DataflowVariable<>();
        task(() -> {
            initialDistance.set(100);
            acceleration.set(2);
            time.set(10);
        });

        int result = initialDistance.get() + acceleration.get() / 2 * (time.get() * time.get());
        assertEquals(result, 200);
    }

    @Test
    public void testWhenBound() {
        DataflowVariable<String> dv = new DataflowVariable<>();
        dv.whenBound((newValue) -> {
            System.out.println("New value is:"+newValue);
            text = "New value is:" + newValue;
        });
        task(() -> dv.set("VALUE")).join();
        assertEquals("New value is:VALUE", text);
    }

    @Test
    public void testThen() {
        DataflowVariable<String> dv = new DataflowVariable<>();
        dv.then((value) -> value.toUpperCase()).
        then((value) -> value + value).
        then((value) -> {
            text = "New value is:" + value;
            return value;
        });

        task(() -> dv.set("value")).join();
        assertEquals("New value is:VALUEVALUE", text);
    }

    int total;

    synchronized void addToTotal(Integer amount) {
        total += amount;
    }

    @Test
    public void allListenValue() throws InterruptedException {
        total = 0;
        int number = 2000;
        DataflowVariable<Integer> dv = new DataflowVariable<>();

        for (int i = 0; i < number; i++) {
            task(() -> {
                try {
                    addToTotal(dv.get());
                } catch (Exception e) {
                    System.out.println("Exception message: " + e.getMessage());
                }
            }).then(() -> {
                addToTotal(1);
            });
        }
        dv.set(3);
        Thread.sleep(600);
        assertEquals(number * 4, total);
    }
}
