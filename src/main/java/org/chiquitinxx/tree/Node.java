package org.chiquitinxx.tree;

/**
 * Created by jorge on 2/4/15.
 */
public class Node<T> {

    private T value;
    private Node<T> left;
    private Node<T> right;

    public Node(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getLeft() {
        return this.left;
    }

    public void setLeft(Node<T> node) {
        this.left = node;
    }

    public Node<T> getRight() {
        return this.right;
    }

    public void setRight(Node<T> node) {
        this.right = node;
    }

    @Override
    public String toString() {
        return "(" + getLeft() + " - " + value + " - " + getRight() + ")";
    }
}
