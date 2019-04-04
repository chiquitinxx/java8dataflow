package yila.experiments.winter

import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll
import yila.experiments.winter.persistence.PersonRepository
import yila.experiments.winter.persistence.RepositoryException
import yila.experiments.winter.persistence.RepositoryFactory
import yila.experiments.winter.visible.Error
import yila.experiments.winter.visible.Person
import yila.experiments.winter.visible.Result

/**
 * JFL 16/11/18
 */
@Ignore
class CreatePersonSpec extends Specification {

    void 'create a person with name and age'() {
        given:
        String name = 'Jorge'
        int age = 42
        
        when:
        Result result = CreatePersonUseCase.createPerson(name, age)

        then:
        result.getValue().get() == new Person(name, age)
    }

    @Unroll
    void 'error creating person with name #name'() {
        when:
        Result result = CreatePersonUseCase.createPerson(name, 42)

        then:
        result.error.get() == Error.INVALID_PERSON_NAME

        where:
        name << [null, '', '    ', 'b', '  c']
    }

    @Unroll
    void 'error creating person with age #age'() {
        when:
        Result result = CreatePersonUseCase.createPerson('Jorge', age)

        then:
        result.error.get() == Error.INVALID_PERSON_AGE

        where:
        age << [-1, 150]
    }

    void 'error if fails storing person in repository'() {
        given:
        String name = 'Jorge'
        int age = 42
        RepositoryFactory.setPersonRepository(Stub(PersonRepository) {
            save(_) >> { throw new RepositoryException() }
        })

        when:
        Result result = CreatePersonUseCase.createPerson(name, age)

        then:
        result.error.get()
    }
}
