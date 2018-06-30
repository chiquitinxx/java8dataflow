package yila.experiments.frp.stream;

import yila.experiments.frp.event.Event;

import java.util.function.Consumer;

/**
 * Created by jorgefrancoleza on 17/10/15.
 */
public interface Publisher<T> {
    void stop() throws InterruptedException;
    void publish(T message);
    void complete();
    void error(Throwable t);
    void subscribe(Consumer<Event<T>> subscriber);
    void unsubscribe(Consumer<Event<T>> subscriber);
}
