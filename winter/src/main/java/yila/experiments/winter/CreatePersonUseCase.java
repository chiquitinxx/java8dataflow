package yila.experiments.winter;

import java.util.function.Function;
import java.util.function.Supplier;

import static yila.experiments.winter.RepositoryFactory.onPersonRepository;
import static yila.experiments.winter.StringValidation.trimLengthAtLeast;
import static yila.experiments.winter.Result.ok;
import static yila.experiments.winter.Result.validate;

/**
 * JFL 17/11/18
 */
public class CreatePersonUseCase {

    public static Result<Person> createPerson(String name, int age) {
        return validateName(name)
                .then(validateAge(age))
                .then(buildPerson(name, age))
                .then(savePerson);
    }

    private static Result<Validation> validateName(String name) {
        return validate(trimLengthAtLeast(name, 2), Error.INVALID_PERSON_NAME);
    }

    private static Supplier<Result<Validation>> validateAge(int age) {
        return () -> validate(age >= 0 && age < 150, Error.INVALID_PERSON_AGE);
    }

    private static Supplier<Result<Person>> buildPerson(String name, int age) {
        return () -> ok(new Person(name, age));
    }

    private static Function<Person, Result<Person>> savePerson =
        person -> onPersonRepository(repository -> ok(repository.save(person)));
}
