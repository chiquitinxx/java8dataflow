package yila.experiments.winter.persistence;

import yila.experiments.winter.visible.BasicError;
import yila.experiments.winter.visible.PersistenceError;
import yila.experiments.winter.visible.Result;

/**
 * JFL 17/11/18
 */
public class RepositoryFactory {

    private static PersonRepository personRepository;

    public static <T> Result<T> onPersonRepository(PersonRepositoryFunction<PersonRepository, Result<T>> consumer) {

        try {
            return consumer.apply(personRepository);
        } catch (RepositoryException repositoryException) {
            //System.out.println(repositoryException.getMessage());
        }
        return Result.error(new BasicError<>(PersistenceError.ERROR_PERSON_REPOSITORY));
    }

    public static void setPersonRepository(PersonRepository repository) {
        personRepository = repository;
    }
}
