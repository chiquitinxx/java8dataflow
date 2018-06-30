package org.chiquitinxx.funz;

/**
 * Created by jorgefrancoleza on 4/12/16.
 */
public class Pair<U, V> {
    private final U first;
    private final V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }
}
