package org.chiquitinxx.funz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
@RunWith(JUnit4.class)
public class LazyValueTest {

    @Test
    public void testReturnValues() {
        Value<String> value = new LazyValue<>(() -> "hello!");
        assertEquals(value.get(), "hello!");
    }

    @Test
    public void testSupplierRunsWithEachGet() {
        String chars = "ch";
        Value<String> value = new LazyValue<>(() -> chars + chars);
        String result = value.get();
        assertEquals(result, "chch");
        assertFalse(result == "chch");
        assertFalse(result == value.get());
    }
}
