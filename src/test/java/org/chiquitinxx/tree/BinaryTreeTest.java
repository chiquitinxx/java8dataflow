package org.chiquitinxx.tree;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Created by jorge on 2/4/15.
 */
@RunWith(JUnit4.class)
public class BinaryTreeTest {
    @Test
    public void testAsList() {
        tree.insert(4).insert(7).insert(1).insert(2).insert(5).insert(9).insert(3);
        assertEquals(tree.asList(), Arrays.asList(1, 2, 3, 4, 5, 7, 9));
    }

    private BinaryTree<Integer> tree = new BinaryTree<>();
}
