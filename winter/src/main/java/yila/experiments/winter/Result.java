package yila.experiments.winter;

import java.util.Collections;
import java.util.List;
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
        return new Result<>(null, Collections.singletonList(error));
    }

    public static Result errors(List<Error> errors) {
        return new Result<>(null, errors);
    }

    public static Result<Validation> validate(boolean success, Error error) {
        return success ? ok(Validation.OK): error(error);
    }

    private final boolean ok;
    private final T result;
    private final List<Error> errors;

    private Result(T result, List<Error> errors) {
        this.result = result;
        this.ok = result != null;
        this.errors = errors;
    }

    boolean isOk() {
        return ok;
    }

    Optional<T> getValue() {
        return Optional.ofNullable(result);
    }

    boolean hasError(Error error) {
        return errors != null && errors.contains(error);
    }

    <U> Result<U> then(Supplier<Result<U>> supplier) {
        return this.then(value -> supplier.get());
    }

    <U> Result<U> then(Function<T, Result<U>> function) {
        if (this.isOk()) {
            return function.apply(this.result);
        } else {
            return (Result<U>)this;
        }
    }
}
