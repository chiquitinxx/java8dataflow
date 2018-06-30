package yila.experiments.frp.property

import spock.util.concurrent.PollingConditions
import yila.experiments.frp.executor.LambdaExecutorSpec

import java.util.concurrent.ForkJoinPool

/**
 * Created by jorgefrancoleza on 23/9/15.
 */
class PropertySpec extends LambdaExecutorSpec {

    def 'set and get value from a property'() {
        given:
        def property = new Property(0)

        expect:
        property.value == 0

        when:
        property.value = [1, 2, 4]

        then:
        property.value == [1, 2, 4]
    }

    def 'do something synchronous when value changes'() {
        given:
        def property = new Property(0)
        def value = 0

        when:
        property.onChange { oldValue, newValue ->
            if (oldValue == 0 && newValue == 35)
                value = 1
        }
        property.value = 35

        then:
        value == 1
    }

    def 'do asynchronous task when value changes'() {
        given:
        def conditions = new PollingConditions()
        def property = new Property(0)
        def value = 0

        when:
        property.onChangeAsync { oldValue, newValue ->
            if (oldValue == 0 && newValue == 35)
                value = 1
        }
        property.value = 35

        then:
        conditions.eventually {
            assert value == 1
        }
    }

    def 'use non blocking queue to run a lot of asynchronous tasks'() {
        given:
        def conditions = new PollingConditions(timeout: 4)
        def pool = new ForkJoinPool()
        def property = new Property(0, pool)
        def closure = { oldValue, newValue ->
            property.value = property.value + 1
        }
        6.times {
            property.onChangeAsync closure
        }

        when:
        property.value = 1

        then:
        notThrown(OutOfMemoryError)
        conditions.eventually {
            assert property.value > 100000
        }

        cleanup:
        property.stop()
    }

    def 'not stop notify changes if listeners are slow on async mode'() {
        given:
        def conditions = new PollingConditions()
        def property = new Property(0)
        def executions = 0
        def closure = { oldValue, newValue ->
            executions ++
            property.value = property.value + 1
            sleep(1000)
        }
        property.onChangeAsync closure

        when:
        property.value = 1

        then:
        conditions.eventually {
            assert executions > 400
        }

        cleanup:
        property.stop()
    }
}
