package org.chiquitinxx.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 2/4/15.
 */
public class BinaryTree<T extends Comparable<T>> {
    private Node<T> first;

    public BinaryTree() {
        first = null;
    }

    public BinaryTree<T> insert(T value) {
        if (first == null) {
            first = new Node<>(value);
        } else {
            insertNewValue(first, value);
        }
        return this;
    }

    public List<T> asList() {
        List<T> result = new ArrayList<>();
        traversal(first, result);
        return result;
    }

    private void insertNewValue(Node<T> node, T value) {
        int compare = value.compareTo(node.getValue());
        if (compare < 0) {
            if (node.getLeft() != null) {
                insertNewValue(node.getLeft(), value);
            } else {
                node.setLeft(new Node<T>(value));
            }
        }
        if (compare > 0) {
            if (node.getRight() != null) {
                insertNewValue(node.getRight(), value);
            } else {
                node.setRight(new Node<T>(value));
            }
        }
    }

    private void traversal(Node<T> node, List<T> list) {
        if (node != null) {
            traversal(node.getLeft(), list);
            list.add(node.getValue());
            traversal(node.getRight(), list);
        }
    }
}
