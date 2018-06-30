package yila.experiments.acme.tools;

import java.util.function.Consumer;

/**
 * JFL 19/6/18
 */
public interface ConsumerList<T> {

    ConsumerList<T> add(Consumer<T> consumer);

    T launch(T event);
}
