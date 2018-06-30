package org.chiquitinxx.concurrency;

/**
 * Created by jorgefrancoleza on 24/9/15.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RandomTest {

    @Test
    public void testUsingCompletableTask()  {
        CountDownLatch latch = new CountDownLatch(6);
        Random random = new Random();
        AtomicInteger atomic = new AtomicInteger(0);
        AtomicInteger setteds = new AtomicInteger(0);
        AtomicInteger dones = new AtomicInteger(0);
        AtomicInteger goings = new AtomicInteger(0);

        while (latch.getCount() > 0) {
            CompletableFuture.supplyAsync(() -> random.nextInt(50))
                    .thenAcceptAsync((val) -> {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                        }
                        if (atomic.get() < val)
                            atomic.set(val);
                        setteds.incrementAndGet();
                    }).thenRunAsync(() -> {
                        latch.countDown();
                        dones.incrementAndGet();
                    });
            goings.incrementAndGet();
        }

        System.out.println("Goings " + goings.get());
        System.out.println("Setted " + setteds.get());
        System.out.println("Done " + dones.get());
        System.out.println("Max value " + atomic.get());
        assertTrue(atomic.get() > 0);
    }
}
