package org.chiquitinxx.concurrency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.chiquitinxx.concurrency.Task.task;
import static org.chiquitinxx.concurrency.Task.whenAllBound;
import static org.junit.Assert.assertEquals;

/**
 * Created by jorge on 15/05/14.
 */
@RunWith(JUnit4.class)
public class DataflowQueueTest {

    @Test
    public void testDataflowQueue() throws ExecutionException, InterruptedException {
        DataflowQueue<String> queue = new DataflowQueue<>();
        queue.set("1");
        queue.set("2");
        queue.set("3");
        assertEquals(queue.getSize(), 3);
        assertEquals(queue.get(), "1");
        assertEquals(queue.get(), "2");
        assertEquals(queue.get(), "3");
    }

    @Test
    public void testStress() throws ExecutionException, InterruptedException {
        int number = 1000;
        Date date = new Date();
        DataflowQueue<Integer> queue = new DataflowQueue<>();


        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < number / 2; i++) {
            //futures.add(queue);
            task(() -> futures.add(queue));
        }

        for (int i = 0; i < number; i++) {
            queue.set(i);
        }

        for (int i = number / 2; i < number; i++) {
            futures.add(task(() -> queue.get()));
        }

        whenAllBound((list) -> {
            System.out.println("Time: "+(new Date().getTime() - date.getTime()));
            assertEquals(list.length, number);
            int sum = Arrays.asList(list).stream().mapToInt(e -> (Integer)e).sum();
            assertEquals(sum, 499500);
            System.out.println("End.");
        }, futures);
    }
}
