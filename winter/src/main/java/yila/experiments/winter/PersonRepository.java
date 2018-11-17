package yila.experiments.winter;

/**
 * JFL 17/11/18
 */
public interface PersonRepository {
    Person save(Person person) throws RepositoryException;
}
