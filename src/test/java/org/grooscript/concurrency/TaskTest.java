package org.grooscript.concurrency;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.grooscript.concurrency.Task.*;

/**
 * Created by jorge on 15/05/14.
 */
@RunWith(JUnit4.class)
public class TaskTest {

    volatile int number;
    String info;

    @Test
    public void testExecuteTask() throws InterruptedException {
        number = 0;
        task((Runnable)() -> number = 1);
        Thread.sleep(50);
        assertEquals(1, number);
    }

    @Test
    public void testExecuteTaskWithThen() throws InterruptedException {
        number = 0;
        task((Runnable)() -> number = 3).then(() -> {
            number = number + 5;
        }).then(() -> number = number + 4);
        Thread.sleep(100);
        assertEquals(12, number);
    }

    @Test
    public void testExecuteTaskOnError() throws InterruptedException {
        number = 0;
        task(() -> {
            List<Integer> list = null;
            list.add(5);
        }).onError((t) -> {
            number = -1;
        });
        Thread.sleep(50);
        assertEquals(-1, number);
    }


    @Test
    public void testExecuteTaskThatReturnsFuture() throws Exception {
        Future result = task(() -> {
            Thread.sleep(10);
            return 5;
        });
        assertEquals(5, result.get());

        assertEquals("Hello world!", task(() -> "Hello world!").get());
    }

    @Test
    public void testWhenAllBound() throws Exception {
        info = "";
        DataflowVariable<String> hello = new DataflowVariable<>();
        Future world = task(() -> {
            hello.set("Hello");
            return "World";
        });
        whenAllBound((values -> info = values[0] + " - " + values[1]), hello, world);
        Thread.sleep(50);
        assertEquals("Hello - World", info);
    }

    @Test
    public void testJoinRunnableTask() throws Exception {
        number = 0;
        TaskResult result = task(() -> {
            try { Thread.sleep(10); } catch (InterruptedException ie) { ie.printStackTrace();}
            number = 5;
        });
        result.join();
        assertEquals(5, number);
    }

    @Ignore("Launching from Gradle deadlock shows, not executing from idea.")
    public void testDeadLock() throws Exception {
        DataflowVariable<Integer> a = new DataflowVariable<>();
        DataflowVariable<Integer> b = new DataflowVariable<>();
        task(() -> {
            try {
                System.out.println("b:" + b.get());
            } catch (Exception e) { e.printStackTrace(); }
            a.set(5);
        });
        task(() -> {
            try {
                System.out.println("a:" + a.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            b.set(5);
        });
        assertEquals(a.get(), b.get());
    }

    @Test
    public void testReturningFromTwoTasks() throws Exception {
        Future a = task(() -> 10);
        Future b = task(() -> 20);
        ArrayList<Integer> list = new ArrayList<>();
        whenAllBound((values -> {
            list.add((Integer)values[0]);
            list.add((Integer)values[1]);
        }), a, b);
        Thread.sleep(50);
        assertEquals(list.stream().mapToInt(p -> p).sum(), 30);
    }

}
