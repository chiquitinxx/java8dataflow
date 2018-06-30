package yila.experiments.acme.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * JFL 19/6/18
 */
public class ConsumerListImpl<T> implements ConsumerList<T> {

    private List<Consumer<T>> listeners = new ArrayList<>();

    @Override
    public ConsumerList<T> add(Consumer<T> consumer) {
        listeners.add(consumer);
        return this;
    }

    @Override
    public T launch(T event) {
        listeners.forEach(consumer -> consumer.accept(event));
        return event;
    }
}
