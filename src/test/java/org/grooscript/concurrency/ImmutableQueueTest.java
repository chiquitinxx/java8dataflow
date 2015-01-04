package org.grooscript.concurrency;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.grooscript.concurrency.Task.task;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: jorgefrancoleza
 * Date: 29/08/14
 */
@RunWith(JUnit4.class)
public class ImmutableQueueTest {

    @Test
    public void testPopulateQueue() {
        ImmutableQueue<String> queue = new ImmutableQueue<>();
        assertEquals(queue.isEmpty(), true);
        queue.add("a");
        assertEquals(queue.isEmpty(), false);
        queue.add("b");
        assertEquals(queue.remove(), "a");
        queue.add("c");
        assertEquals(queue.remove(), "b");
        assertEquals(queue.remove(), "c");
        assertEquals(queue.isEmpty(), true);
        assertEquals(queue.remove(), null);
    }

    @Test
    public void testAddItemsSameTime() throws InterruptedException {
        int number = 1000;
        ImmutableQueue<String> queue = new ImmutableQueue<>();

        for (int i = 0; i < number; i++) {
            task(() -> queue.add("A"));
        }
        Thread.sleep(1);
        for (int i = 0; i < number; i++) {
            task(() -> assertEquals(queue.remove(), "A"));
        }
    }

    @Test
    public void testAddAndRemove() throws InterruptedException {
        int number = 1000;
        ImmutableQueue<String> queue = new ImmutableQueue<>();

        for (int i = 0; i < number; i++) {
            task(() -> {
                queue.add("A");
                task(() -> assertEquals(queue.remove(), "A"));
            });
        }
    }
}
