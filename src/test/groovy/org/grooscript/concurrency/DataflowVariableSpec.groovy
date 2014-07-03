package org.grooscript.concurrency

import spock.lang.Specification
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger

import static org.grooscript.concurrency.Task.task

/**
 * Created by jorge on 14/05/14.
 */
class DataflowVariableSpec extends Specification {

    private static String VALUE = 'value'
    private static Integer NUMBER = 5

    void 'wait dataflow variable'() {
        given:
        def dv = new DataflowVariable<String>()

        expect:
        !dv.isDone()

        when:
        task {
            dv.set(VALUE)
        }

        then:
        dv.get() == VALUE
        !dv.isCancelled()
        dv.isDone()
    }

    void 'cancel dataflow variable thread with interrupt'() {
        given:
        def dv = new DataflowVariable<Integer>()

        expect:
        !dv.isCancelled()
        !dv.isDone()

        when:
        dv.cancel(true)
        dv.get()

        then:
        thrown InterruptedException
        dv.isDone()
        dv.isCancelled()
    }

    void 'dataflow timeout with get timeout'() {
        given:
        def dv = new DataflowVariable<Integer>()

        when:
        dv.get(50, TimeUnit.NANOSECONDS)

        then:
        thrown TimeoutException
    }

    void 'cancel dataflow variable thread without interrupt'() {
        given:
        def dv = new DataflowVariable<Integer>()

        expect:
        !dv.isCancelled()
        !dv.isDone()

        when:
        dv.cancel(false)
        def result = dv.get()

        then:
        dv.isDone()
        dv.isCancelled()
        result == null
    }

    void 'combine dataflow variables'() {
        given:
        final def x = new DataflowVariable()
        final def y = new DataflowVariable()
        final def z = new DataflowVariable()

        when:
        task {
            z.set(x.get() + y.get())
        }

        task {
            x.set(10)
        }

        task {
            y.set(5)
        }

        then:
        z.get() == 15
    }

    void 'when bound dataflow variable'() {
        given:
        def text = ''
        final def dv = new DataflowVariable<String>()
        dv.whenBound {
            text = "dataflow value is:${it}"
        }

        when:
        task {
            dv.set(VALUE)
        }

        then:
        dv.get() == VALUE
        text == 'dataflow value is:value'
    }

    def 'waiting some threads reading dataflow variable'() {
        given:
        DataflowVariable<Integer> integerDataflow = new DataflowVariable<Integer>()
        DataflowQueue<Integer> queue = new DataflowQueue<Integer>()
        int numberOfTimes = 100
        int increment = 5
        AtomicInteger total = new AtomicInteger(0)
        int i

        when:
        for (i = 0; i < numberOfTimes; i++) {
            task {
                queue.set(total.getAndAdd(integerDataflow.get()))
            }
        }
        task {
            integerDataflow.set(new Integer(increment))
        }
        numberOfTimes.times { it ->
            //println queue.get()
            queue.get()
        }

        then:
        total.get() == increment * numberOfTimes
    }

    void 'then after some variable is bounded'() {
        given:
        final def dv = new DataflowVariable<Integer>()
        dv.then {
            it * 2
        }.then {
            it + 1
        }

        when:
        task {
            dv.set(NUMBER)
        }

        then:
        dv.get() == 11
    }
}
