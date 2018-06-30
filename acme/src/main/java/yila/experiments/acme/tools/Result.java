package yila.experiments.acme.tools;

import java.util.function.Consumer;

/**
 * JFL 18/6/18
 */
public class Result<T> {

    private final T result;
    private final Error error;

    private Result(T result, Error error) {
        this.result = result;
        this.error = error;
    }
    
    public static <T> Result success(T result) {
        if (result == null) {
            throw new RuntimeException("Can't create null result");
        }
        return new Result<>(result, null);
    }

    public static <T> Result fail(Error error) {
        return new Result<T>(null, error);
    }

    public boolean isOk() {
        return result != null;
    }

    public Result<T> ifOk(Consumer<T> consumer) {
        if (isOk()) {
            consumer.accept(result);
        }
        return this;
    }

    public Result<T> ifFailed(Consumer<Error> consumer) {
        if (!isOk()) {
            consumer.accept(error);
        }
        return this;
    }

    @Override
    public String toString() {
        if (isOk()) {
            return "Ok(⭐)";
        } else {
            return "Error(" + this.error.getCode() + ")";
        }
    }
}
