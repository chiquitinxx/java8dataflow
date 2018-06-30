package org.chiquitinxx.funz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

/**
 * Created by jorgefrancoleza on 04/12/16.
 */
@RunWith(JUnit4.class)
public class PairTest {

    @Test
    public void testPairValues() {
        Pair<Value<Integer>, MaybeValue<String>> pair = new Pair<>(
                new ImmediateValue<>(1),
                new AsyncImmediateValue<>(() -> "hello world!"));
        assertEquals(pair.getFirst().get(), new Integer(1));
        assertEquals(pair.getSecond().get(), "hello" + " " + "world!");
    }
}
