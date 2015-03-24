package org.chiquitinxx.concurrency;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.chiquitinxx.concurrency.Task.*;

/**
 * Created by jorge on 15/05/14.
 */
@RunWith(JUnit4.class)
public class TaskTest {

    volatile int number;
    String info;

    @Test
    public void testExecuteTask()  {
        number = 0;
        Task.task((Runnable) () -> number = 1);
        waitMilis(10);
        assertEquals(1, number);
    }

    @Test
    public void testTaskRunnableIsAsync()  {
        number = 0;
        task(() -> {
            waitMilis(50);
            number = 1;
        });
        assertEquals(0, number);
    }

    @Test
    public void testTaskCallableIsAsync()  {
        number = 0;
        task(() -> {
            waitMilis(50);
            number = 1;
            return 1;
        });
        assertEquals(0, number);
    }

    @Test
    public void testExecuteTaskWithThen()  {
        number = 0;
        Task.task((Runnable) () -> number = 3).then(() -> {
            number = number + 5;
        }).then(() -> number = number + 4);
        waitMilis(50);
        assertEquals(12, number);
    }

    @Test
    public void testExecuteTaskOnError() {
        number = 0;
        task(() -> {
            List<Integer> list = null;
            list.add(5);
        }).onError((t) -> {
            number = -1;
        });
        waitMilis(100);
        assertEquals(-1, number);
    }


    @Test
    public void testExecuteTaskThatReturnsFuture() throws Exception {
        Future result = task(() -> {
            waitMilis(10);
            return 5;
        });
        assertEquals(5, result.get());

        assertEquals("Hello world!", task(() -> "Hello world!").get());
    }

    @Test
    public void testWhenAllBound() {
        info = "";
        DataflowVariable<String> hello = new DataflowVariable<>();
        Future world = task(() -> {
            hello.set("Hello");
            return "World";
        });
        whenAllBound((values -> info = values.get(0) + " - " + values.get(1)), hello, world);
        waitMilis(50);
        assertEquals("Hello - World", info);
    }

    @Test
    public void testWhenAllBoundIsAsync() {
        info = "";
        DataflowVariable<String> hello = new DataflowVariable<>();
        Future world = task(() -> {
            waitMilis(20);
            hello.set("Hello");
        });
        whenAllBound((values -> info = values.get(0) + " - " + values.get(1)), hello, world);
        assertTrue(hello.notHasValue());
    }

    @Test
    public void testJoinRunnableTask() {
        number = 0;
        FutureResult result = task(() -> {
            waitMilis(10);
            number = 5;
        });
        result.join();
        assertEquals(5, number);
    }

    @Ignore("Deadlock")
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
    public void testReturningFromTwoTasks() {
        Future<Integer> a = task(() -> 10);
        Future<Integer> b = task(() -> 20);
        ArrayList<Integer> list = new ArrayList<>();
        whenAllBound((values -> {
            list.add(values.get(0));
            list.add(values.get(1));
        }), a, b);
        waitMilis(50);
        assertEquals(list.stream().mapToInt(p -> p).sum(), 30);
    }

    private void waitMilis(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ie) {
            //nothing to do
        }
    }
}
