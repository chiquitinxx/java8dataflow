package yila.experiments.winter.persistence;

import yila.experiments.winter.CreatePersonUseCase;
import yila.experiments.winter.visible.Person;
import yila.experiments.winter.visible.Result;
import yila.experiments.winter.visible.Winter;

import java.util.function.Function;

import static yila.experiments.winter.persistence.RepositoryFactory.onPersonRepository;
import static yila.experiments.winter.visible.Result.ok;

/**
 * JFL 19/11/18
 */
public class WinterPersistence implements Winter {

    @Override
    public Result<Person> createPerson(String name, int age) {
        return createAndSavePerson(name, age);
    }

    @Override
    public Result<Person> updatePerson(Person oldPerson, Person newPerson) {
        return null;
    }

    private Result<Person> createAndSavePerson(String name, int age) {
        //Handle with constraints, transactionality,....
        return CreatePersonUseCase.createPerson(name, age).ifOk(savePerson);
    }

    private static Function<Person, Result<Person>> savePerson =
            person -> onPersonRepository(repository -> ok(repository.save(person)));
}
