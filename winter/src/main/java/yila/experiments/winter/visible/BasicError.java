package yila.experiments.winter.visible;

/**
 * JFL 19/11/18
 */
public class BasicError<T> implements Error<T> {

    private final T data;

    public BasicError(T error) {
        this.data = error;
    }

    @Override
    public T getData() {
        return data;
    }
}
