package yila.experiments.winter.persistence;

import yila.experiments.winter.visible.Person;

/**
 * JFL 17/11/18
 */
public interface PersonRepository {
    Person save(Person person) throws RepositoryException;
}
