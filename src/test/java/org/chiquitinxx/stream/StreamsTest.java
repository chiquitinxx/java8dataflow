package org.chiquitinxx.stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.stream.*;

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
        Assert.assertEquals(reduce.toString(), "1hello");
    }

    private List things = new ArrayList<>();
}
