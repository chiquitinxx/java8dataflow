package yila.experiments.winter.visible;

import yila.experiments.winter.Validation;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * JFL 16/11/18
 */
public class Result<T> {

    public static <T> Result<T> ok(T result) {
        return new Result<>(result, null);
    }

    public static Result error(Error error) {
        return new Result<>(null, error);
    }

    public static Result<Validation> validate(boolean success, Error error) {
        return success ? ok(Validation.OK): error(error);
    }
    
    private final T result;
    private final Error error;

    private Result(T result, Error error) {
        this.result = result;
        this.error = error;
    }

    public boolean isOk() {
        return result != null;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(result);
    }

    public Optional<Error> getError() {
        return Optional.ofNullable(this.error);
    }

    public <U> Result<U> ifOk(Supplier<Result<U>> supplier) {
        return this.ifOk(value -> supplier.get());
    }

    public <U> Result<U> ifOk(Function<T, Result<U>> function) {
        if (this.isOk()) {
            return function.apply(this.result);
        } else {
            return (Result<U>)this;
        }
    }
}
