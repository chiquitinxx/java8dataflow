package org.chiquitinxx.lambda;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by jorgefrancoleza on 25/3/15.
 */
@RunWith(JUnit4.class)
public class ExperimentsTest {

    private List<Integer> originalList = Arrays.asList(1, 9, 6, 5, 4, 2, 8, 11, 3);
    private List<Integer> sortedList = Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 11);
    private Experiments experiments = new Experiments();

    @Test
    public void testMergeSort()  {
        List<Integer> values = originalList;
        assertEquals(values, originalList);
        assertEquals(experiments.mergeSort(values), sortedList);
        Collections.shuffle(values);
        assertThat(values, not(sortedList));
        assertEquals(experiments.mergeSort(values), sortedList);
    }

    @Test
    public void testGenerateList()  {
        List<Integer> values = experiments.generateRandomList(100);
        List<Integer> sorted = values.stream()
                .sorted()
                .collect(Collectors.toList());
        assertEquals(experiments.mergeSort(values), sorted);
    }

    @Test
    public void testSpeedMergeSort()  {
        List<Integer> values = Arrays.asList(100, 1000, 10000);
        values.forEach(number -> {
            List<Integer> list = experiments.generateRandomList(number);
            Date date = new Date();
            experiments.mergeSort(list);
            System.out.println("Merge sort time for " + number + " elements: " + (new Date().getTime() - date.getTime()));
        });

        values.forEach(number -> {
            List<Integer> list = experiments.generateRandomList(number);
            Date date = new Date();
            Collections.sort(list);
            System.out.println("Collections sort time for " + number + " elements: " + (new Date().getTime() - date.getTime()));
        });
    }

    @Test
    public void testGenerateNumbers() {
        experiments.generateNumbers();
    }
}
