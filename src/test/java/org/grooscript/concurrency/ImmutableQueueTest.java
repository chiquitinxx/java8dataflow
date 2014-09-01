package org.grooscript.concurrency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.grooscript.concurrency.Task.task;
import static org.junit.Assert.assertEquals;

/**
 * User: jorgefrancoleza
 * Date: 29/08/14
 */
@RunWith(JUnit4.class)
public class ImmutableQueueTest {

    @Test
    public void testAddItemsSameTime() throws InterruptedException {
        int number = 1000;
        ImmutableQueue<String> queue = new ImmutableQueue<>();

        for (int i = 0; i < number; i++) {
            task(() -> queue.add("A"));
        }
        Thread.sleep(10);
        for (int i = 0; i < number; i++) {
            assertEquals(queue.remove(), "A");
        }
    }
}
