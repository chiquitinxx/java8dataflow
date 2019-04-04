package yila.experiments.winter.persistence;

/**
 * JFL 17/11/18
 */
@FunctionalInterface
public interface PersonRepositoryFunction<PersonRepository, T> {

    T apply(PersonRepository personRepository) throws RepositoryException;
}
