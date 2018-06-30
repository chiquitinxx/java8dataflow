package org.chiquitinxx.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jorgefrancoleza on 25/3/15.
 */
public class Experiments {

    public List<Integer> generateRandomList(int size) {
        Random random = new Random();
        return Stream.generate(random::nextInt)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Integer> mergeSort(List<Integer> values) {
        return sort(values, 0, values.size() - 1);
    }

    private List<Integer> sort(List<Integer> list, int initial, int end) {
        if (end - initial < 1) {
            List<Integer> elementList = new ArrayList<>();
            elementList.add(list.get(initial));
            return elementList;
        }
        int middle = (initial + end) / 2;
        List<Integer> left = sort(list, initial, middle);
        List<Integer> right = sort(list, middle + 1, end);
        return merge(left, right);
    }

    private List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> result = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0, total = left.size() + right.size();
        while (leftIndex + rightIndex < total) {
            if (leftIndex >= left.size()) {
                result.add(right.get(rightIndex++));
            } else if (rightIndex >= right.size()) {
                result.add(left.get(leftIndex++));
            } else {
                result.add( left.get(leftIndex) < right.get(rightIndex) ?
                        left.get(leftIndex++): right.get(rightIndex++));
            }
        }
        return result;
    }

    public void generateNumbers() {
        Random random = new Random();
        initRepeateds();
        Stream.generate(random::nextInt).limit(1000).parallel().forEach(this::findRepeateds);
    }

    private List<Integer> numbers;

    private void initRepeateds() {
        numbers = new ArrayList<>();
    }

    private void findRepeateds(int number) {
        if (numbers.contains(number)) {
            System.out.println("Repeated: "+number+ " numbers saved: " +numbers.size());
        } else {
            numbers.add(number);
        }
    }
}
