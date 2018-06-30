package org.chiquitinxx.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by jorgefrancoleza on 04/12/16.
 */
@RunWith(JUnit4.class)
public class RxValueTest {

    @Test
    public void testReactToValueIsAsync() throws InterruptedException {
        number = 0;
        ReactiveValue<String> value = new RxValue<>(() -> "hello");
        value.onDone((stringValue) -> {
            try {
                Thread.sleep(10);
                number += stringValue.length();
            } catch (Exception e) {

            }
        });
        assertEquals(number, 0);
        Thread.sleep(100);
        assertEquals(number, 5);
    }

    @Test
    public void testOnDoneOnlyAllowedOneTime() {
        number = 0;
        ReactiveValue<String> value = new RxValue<>(() -> "hello");
        value.onDone((stringValue) -> number += stringValue.length());
        try {
            value.onDone((stringValue) -> number = 0);
            fail("Must fail");
        } catch (RuntimeException re) {
            //
        }
    }

    @Test
    public void testOnError() throws InterruptedException {
        number = 0;
        ReactiveValue<Integer> value = new RxValue<>(() -> 5 / 0);
        value.onError((error) -> number = 33);
        assertEquals(number, 33);
        value.onDone((intValue) -> number = 25);
        Thread.sleep(100);
        assertEquals(number, 33);
    }

    @Test
    public void testOnErrorAfterOnDone() throws InterruptedException {
        number = 0;
        ReactiveValue<Integer> value = new RxValue<>(() -> 5 / 0);
        value.onDone((intValue) -> number = 25);
        value.onError((error) -> number = 33);
        Thread.sleep(100);
        assertEquals(number, 33);
    }

    private static int number = 0;
}
