package yila.experiments.frp.property;

import yila.experiments.frp.Pair;
import yila.experiments.frp.executor.InitialExecutor;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import static yila.experiments.frp.executor.LambdaExecutor.executeAsync;

/**
 * Created by jorgefrancoleza on 16/10/15.
 */
public class Property <T> extends InitialExecutor {

    private volatile T _safe;
    private List<ChangeConsumer<T>> listeners = new CopyOnWriteArrayList<>();
    private List<ChangeConsumer<T>> asyncListeners = new CopyOnWriteArrayList<>();
    private Queue<Pair<T>> queue = new ConcurrentLinkedQueue<>();

    public Property(T initialValue, ExecutorService executor) {
        this._safe = initialValue;
        assignExecutor(executor).execute(() -> {
            while (running) {
                final Pair<T> pair = queue.poll();
                if (pair != null) {
                    if (!asyncListeners.isEmpty()) {
                        asyncListeners.forEach(
                            (consumer) -> executeAsync(() -> consumer.onChange(pair.getFirst(), pair.getSecond()))
                        );
                    }
                }
            }
        });
    }

    public Property(T initialValue) {
        this(initialValue, defaultExecutor());
    }

    public T getValue() {
        return _safe;
    }

    public void setValue(T value) {
        final T oldValue = _safe;
        _safe = value;
        if (!listeners.isEmpty())
            listeners.forEach(fn -> fn.onChange(oldValue, value));
        queue.add(new Pair<>(oldValue, value));
    }

    public void onChange(ChangeConsumer<T> changeConsumer) {
        listeners.add(changeConsumer);
    }

    public void onChangeAsync(ChangeConsumer<T> changeConsumer) {
        asyncListeners.add(changeConsumer);
    }

    @Override
    public void stop() throws InterruptedException {
        super.stop();
        listeners.clear();
        asyncListeners.clear();
    }
}
