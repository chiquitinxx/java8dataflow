package org.chiquitinxx.stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.stream.*;

import static org.junit.Assert.*;

/**
 * Created by jorge on 10/7/15.
 */
@RunWith(JUnit4.class)
public class StreamsTest {
    @Test
    public void testObjectStream() {
        things.add(1);
        things.add("hello");
        Stream stream = things.stream();
        Object reduce = stream.reduce((x, y) -> String.valueOf(x) + String.valueOf(y)).get();
        assertEquals(reduce.toString(), "1hello");
    }

    @Test
    public void testStreams() {
        IntStream stream = IntStream.of(1, 2, 5, 6, 3, 14);
        assertEquals(stream.limit(5).sum(), 17);
        stream = IntStream.of(1, 2, 5, 6, 3, 14);
        assertEquals(stream.map(i -> i * 2).
                mapToObj(String::valueOf).
                reduce((x, y) -> x + y).
                get(), "241012628");
    }

    private List things = new ArrayList<>();
}
