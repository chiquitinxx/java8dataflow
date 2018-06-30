package yila.experiments.frp.stream

import spock.util.concurrent.PollingConditions
import yila.experiments.frp.executor.LambdaExecutorSpec
import yila.experiments.frp.event.Event
import yila.experiments.frp.event.TypeEvent
import yila.experiments.frp.property.Property

import java.time.LocalDateTime
import java.util.function.Consumer

/**
 * Created by jorgefrancoleza on 17/10/15.
 */
class PublisherSpec extends LambdaExecutorSpec {

    def "basic publisher message"() {
        given:
        def conditions = new PollingConditions()
        def value = 0
        def publisher = FactoryPublisher.newInstance()

        when:
        publisher.subscribe({Event event ->
            assert event.typeEvent == TypeEvent.MESSAGE
            assert event.error == null
            assert event.message == 5
            value = event.message * 2
        })
        publisher.publish(5)

        then:
        value == 0
        conditions.eventually {
            assert value == 10
        }
    }

    def "stop send messages to subscribers"() {
        given:
        def publisher = FactoryPublisher.newInstance()
        def value = 0

        when:
        publisher.subscribe({ e -> value = 5 })
        publisher.stop()
        publisher.publish(4)
        sleep(500)

        then:
        value == 0
    }

    def "subscribe and unsubscribe"() {
        given:
        def conditions = new PollingConditions()
        def value1 = 0, value2 = 0
        def publisher = FactoryPublisher.newInstance()
        def subscriber1 = { value1 += it.message }
        def subscriber2 = { value2 += it.message }

        when:
        publisher.subscribe(subscriber1)
        publisher.subscribe(subscriber2)
        publisher.publish(5)

        then:
        publisher.listeners.size() == 2
        conditions.eventually {
            assert value1 == 5
            assert value2 == 5
        }

        when:
        publisher.unsubscribe(subscriber2)
        publisher.publish(3)
        sleep(500)

        then:
        publisher.listeners.size() == 1
        assert value1 == 8
        assert value2 == 5
    }

    def 'use consumer and then'() {
        given:
        def conditions = new PollingConditions()
        def value = 0
        def publisher = FactoryPublisher.newInstance()
        Consumer<Event<Integer>> subscriber = { value = it.message * 2 }

        when:
        publisher.subscribe(subscriber.andThen({ value = value + it.message }).andThen({ value = value + 2 }))
        publisher.publish(5)

        then:
        value == 0
        conditions.eventually {
            assert value == 17
        }
    }

    def 'recursive publish messages'() {
        given:
        def conditions = new PollingConditions()
        def value = 0
        def publisher = FactoryPublisher.newInstance()
        Consumer<Event<Integer>> subscriber = { value = value + it.message }

        when:
        publisher.subscribe(subscriber
                .andThen({ r -> publisher.publish(3) })
                .andThen({ r -> publisher.publish(2) }))
        publisher.publish(7)

        then:
        conditions.eventually {
            assert value > 10000
        }

        cleanup:
        publisher.stop()
    }

    def 'receive an error'() {
        given:
        def conditions = new PollingConditions()
        def publisher = FactoryPublisher.newInstance()
        def exception = new Throwable("publisher exception")
        def value = 0

        when:
        publisher.subscribe({ Event event ->
            assert event.typeEvent == TypeEvent.ERROR
            assert event.error == exception
            assert event.message == null
            value = 1
        })
        publisher.error(exception)

        then:
        conditions.eventually {
            assert value == 1
        }
    }

    def 'finish producer'() {
        given:
        def conditions = new PollingConditions()
        def publisher = FactoryPublisher.newInstance()
        def value = 0

        when:
        publisher.subscribe({ Event event ->
            assert event.typeEvent == TypeEvent.COMPLETE
            assert event.error == null
            assert event.message == null
            value = 1
        })
        publisher.complete()

        then:
        conditions.eventually {
            assert value == 1
        }
    }

    def 'not stop receiving messages if subscriber is slow'() {
        given:
        def conditions = new PollingConditions()
        def publisher = FactoryPublisher.newInstance()
        def value = 0
        def executions = 0

        when:
        publisher.subscribe({ Event event ->
            executions ++
            publisher.publish(1)
            sleep(1000)
            value += event.message
        })
        publisher.publish(1)

        then:
        conditions.eventually {
            assert value < 2
            assert executions > 400
        }
        println "End"

        cleanup:
        publisher.stop()
    }

    def 'timer instance'() {
        given:
        def conditions = new PollingConditions()
        def value = 0
        def dateTime = LocalDateTime.now()
        def publisher = FactoryPublisher.timerInstance(0, 10)

        when:
        publisher.subscribe({ Event event ->
            value ++
            assert event.message instanceof LocalDateTime
            assert event.message.isAfter(dateTime)
            dateTime = event.message
        })

        then:
        value == 0
        conditions.eventually {
            assert value > 10
        }
    }

    def 'property instance'() {
        given:
        def conditions = new PollingConditions()
        def value = 0
        def property = new Property(0)
        def publisher = FactoryPublisher.propertyInstance(property)

        when:
        publisher.subscribe({ Event event ->
            assert event.message == 3
            value = 99
        })
        property.value = 3

        then:
        conditions.eventually {
            assert value == 99
        }
    }
}
