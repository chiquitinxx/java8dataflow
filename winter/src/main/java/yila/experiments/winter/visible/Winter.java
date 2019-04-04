package yila.experiments.winter.visible;

import yila.experiments.winter.annotation.Adapter;

/**
 * JFL 19/11/18
 */
@Adapter
public interface Winter {

    Result<Person> createPerson(String name, int age);

    Result<Person> updatePerson(Person oldPerson, Person newPerson);
}
