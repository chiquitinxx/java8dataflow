package org.grooscript.concurrency

import spock.lang.Ignore
import spock.lang.Specification

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger

import static org.grooscript.concurrency.Task.task
import static org.grooscript.concurrency.Task.whenAllBound

/**
 * Created by jorge on 14/05/14.
 */
class DataflowQueueSpec extends Specification {

    void 'dataflow queue with some values'() {
        given:
        final DataflowQueue<String> queue = new DataflowQueue<String>()
        def words = ['Groovy', 'fantastic', 'concurrency', 'fun', 'enjoy', 'safe', 'GPars', 'data', 'flow']
        def list = []
        final DataflowVariable<Boolean> allDone = new DataflowVariable<>()

        when:
        task {
            for (word in words) {
                queue.set(word.toUpperCase())
            }
        }
        task {
            for (word in words) {
                list.add queue.get()
            }
            allDone.set true
        }

        then:
        allDone.get()
        list == words*.toUpperCase()
    }

    volatile count = 0

    @Ignore
    void 'stress dataflow queue'() {
        given:
        count = 0
        def number = 10
        final DataflowQueue<String> queue = new DataflowQueue<String>()
        def listFutures = []

        when:
        number.times {
            Thread.start {
                listFutures << doSomeTimes(queue)
            }
        }

        then:
        whenAllBound({ Object[] values ->
            println 'All bound! ' + values
            assert values.size() == number
            assert values.every { it == 'ok' }
            //assert count == number
        }, listFutures)
    }

    private static final NUMBER = 100

    private Future doSomeTimes(queue) {
        DataflowVariable<String> result = new DataflowVariable<>();
        task {
            NUMBER.times {
                queue.set('aaa')
            }
            assert queue.queue.size() == NUMBER
        }.then {
            println 'doSomeTimes'
        }.then {
            NUMBER.times {
                queue.get()
            }
            assert queue.queue.isEmpty()
            println '  finishing!'
            count ++
            result.set('ok')
        }
        return result
    }
}
