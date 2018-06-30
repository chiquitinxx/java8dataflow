package yila.experiments.frp.stream;

import yila.experiments.frp.event.Event;
import yila.experiments.frp.event.TypeEvent;
import yila.experiments.frp.executor.InitialExecutor;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static yila.experiments.frp.executor.LambdaExecutor.executeAsync;

/**
 * Created by jorgefrancoleza on 17/10/15.
 */
public class BasicPublisher<T> extends InitialExecutor implements Publisher<T> {

    private List<Consumer<Event<T>>> listeners = new CopyOnWriteArrayList<>();
    private Queue<Event<T>> queue = new ConcurrentLinkedQueue<>();

    public BasicPublisher(ExecutorService executor) {
        assignExecutor(executor).execute(() -> {
            while (running) {
                final Event<T> event = queue.poll();
                if (event != null) {
                    if (!listeners.isEmpty()) {
                        listeners.forEach(
                                (consumer) -> executeAsync(() -> consumer.accept(event))
                        );
                    }
                }
            }
        });
    }

    public BasicPublisher() {
        this(defaultExecutor());
    }

    @Override
    public void stop() throws InterruptedException {
        super.stop();
        listeners.clear();
    }

    public void publish(T message) {
        Event<T> event = new Event<>(message, TypeEvent.MESSAGE, null);
        queue.add(event);
    }

    public void complete() {
        Event<T> event = new Event<>(null, TypeEvent.COMPLETE, null);
        queue.add(event);
    }

    public void error(Throwable t) {
        Event<T> event = new Event<>(null, TypeEvent.ERROR, t);
        queue.add(event);
    }

    public void subscribe(Consumer<Event<T>> subscriber) {
        listeners.add(subscriber);
    }

    public void unsubscribe(Consumer<Event<T>> subscriber) {
        listeners.remove(subscriber);
    }
}
