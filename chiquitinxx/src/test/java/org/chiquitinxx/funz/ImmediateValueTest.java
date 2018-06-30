package org.chiquitinxx.funz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

/**
 * Created by jorgefrancoleza on 26/11/16.
 */
@RunWith(JUnit4.class)
public class ImmediateValueTest {

    @Test
    public void testReturnValues() {
        Value<String> value = new ImmediateValue<>("hello!");
        assertEquals(value.get(), "hello!");

        Value<Integer> intValue = new ImmediateValue<>(4);
        assertEquals(intValue.get(), new Integer(4));
    }

    @Test
    public void testSupplierRunsOnlyOneTime() {
        String chars = "ch";
        Value<String> value = new ImmediateValue<>(chars + chars);
        String result = value.get();
        assertEquals(result, "chch");
        assertFalse(result == "chch");
        assertTrue(result == value.get());
        assertTrue(result == value.get());
    }

    @Test
    public void testNullableImmediateValue() {
        Value value = new ImmediateValue<String>(null);
        try {
            value.get();
            fail("SupplierReturnsNullException must be thrown");
        } catch (SupplierReturnsNullException e) {
            //
        }
    }
}
