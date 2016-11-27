package org.chiquitinxx.funz;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
@RunWith(JUnit4.class)
public class AsyncImmediateValueTest {

    @Test
    public void testReturnAsyncValue() {
        AtomicInteger atomic = new AtomicInteger();
        assertTrue(atomic.get() == 0);
        MaybeValue<String> maybe = new AsyncImmediateValue<>(() -> {
            try {
                Thread.sleep(50);
                return "hello!" + atomic.getAndAdd(1);
            } catch (Throwable t) {
                //
            }
            return "hello!";
        });
        assertTrue(atomic.get() == 0);
        String value = maybe.get();
        assertTrue(atomic.get() == 1);
        assertEquals(value, "hello!0");
        assertTrue(value == maybe.get());
        assertTrue(atomic.get() == 1);
    }

    @Test
    public void testElseValue() {
        MaybeValue<Integer> maybe = new AsyncImmediateValue<>(() -> 5 / 0);
        try {
            maybe.get();
            fail("Exception must be thrown");
        } catch (NoSuchElementException e) {
            //
        }
        assertTrue(maybe.getError() instanceof ArithmeticException);
        assertEquals(new Integer(7), maybe.orElse(7));
    }

    @Test
    public void testSerializable() {
        AsyncImmediateValue<Integer> maybe = new AsyncImmediateValue<>(() -> 4);
        Serializable copy = SerializationUtils.clone(maybe);
        assertEquals(maybe, copy);
        byte[] serialized = SerializationUtils.serialize(maybe);
        assertEquals(maybe, SerializationUtils.deserialize(serialized));
    }

    @Test
    public void testErrorSerialized() {
        AsyncImmediateValue<Integer> error = new AsyncImmediateValue<>(() -> 5 / 0);
        assertNotNull(error.orElse(9));
        assertNotNull(error.getError());
        byte[] errorSerialized = SerializationUtils.serialize(error);
        assertEquals(error, SerializationUtils.deserialize(errorSerialized));
    }
}
