package org.grooscript.concurrency

import spock.lang.Specification

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger

import static org.grooscript.concurrency.Task.task

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
}
