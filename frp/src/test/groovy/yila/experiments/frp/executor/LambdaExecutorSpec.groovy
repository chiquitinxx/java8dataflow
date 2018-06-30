package yila.experiments.frp.executor

import spock.lang.Specification
import yila.experiments.frp.executor.LambdaExecutor

/**
 * Created by jorgefrancoleza on 18/10/15.
 */
class LambdaExecutorSpec extends Specification {
    def cleanup() {
        def result = LambdaExecutor.stop()
        result.ifPresent({list -> if (list.size() > 0) println "Lambdas not executed: " +list.size()})
    }
}
