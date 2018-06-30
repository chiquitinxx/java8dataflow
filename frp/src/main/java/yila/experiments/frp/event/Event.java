package yila.experiments.frp.event;

/**
 * Created by jorgefrancoleza on 17/10/15.
 */
public class Event<T> {

    private final T message;
    private final TypeEvent typeEvent;
    private final Throwable error;

    public Event(T message, TypeEvent typeEvent, Throwable error) {
        this.message = message;
        this.typeEvent = typeEvent;
        this.error = error;
    }

    public T getMessage() {
        return message;
    }

    public TypeEvent getTypeEvent() {
        return typeEvent;
    }

    public Throwable getError() {
        return error;
    }
}
