package yila.experiments.winter;

import yila.experiments.winter.annotation.UseCase;
import yila.experiments.winter.visible.Person;
import yila.experiments.winter.visible.Result;
import yila.experiments.winter.visible.WinterError;

import java.util.function.Supplier;

import static yila.experiments.winter.StringValidation.trimLengthAtLeast;
import static yila.experiments.winter.visible.Result.ok;
import static yila.experiments.winter.visible.Result.validate;
import static yila.experiments.winter.Validation.buildError;

/**
 * JFL 17/11/18
 */
@UseCase
public class CreatePersonUseCase {

    public static Result<Person> createPerson(String name, int age) {
        return validateName(name)
                .ifOk(validateAge(age))
                .ifOk(buildPerson(name, age));
    }

    private static Result<Validation> validateName(String name) {
        return validate(trimLengthAtLeast(name, 2), buildError(WinterError.INVALID_PERSON_NAME));
    }

    private static Supplier<Result<Validation>> validateAge(int age) {
        return () -> validate(age >= 0 && age < 150, buildError(WinterError.INVALID_PERSON_AGE));
    }

    private static Supplier<Result<Person>> buildPerson(String name, int age) {
        return () -> ok(new Person(name, age));
    }
}
