package yila.experiments.winter;

import static yila.experiments.winter.RepositoryFactory.onPersonRepository;

/**
 * JFL 17/11/18
 */
public class CreatePersonUseCase {

    public static Result createPerson(String name, int age) {
        return validateName(name)
                .then(validateAge(age))
                .then(buildPerson(name, age))
                .then(person -> savePerson((Person)person));
    }

    private static Result<Validation> validateName(String name) {
        return Result.validate(validateNameContent(name), Error.INVALID_PERSON_NAME);
    }

    private static boolean validateNameContent(String name) {
        return name != null && !name.trim().equals("") && name.trim().length() > 1;
    }

    private static Result<Validation> validateAge(int age) {
        return Result.validate(age >= 0 && age < 150, Error.INVALID_PERSON_AGE);
    }

    private static Result<Person> buildPerson(String name, int age) {
        return Result.ok(new Person(name, age));
    }

    private static Result<Person> savePerson(Person person) {
        return (Result<Person>)onPersonRepository(repository -> Result.ok(repository.save(person)));
    }
}
