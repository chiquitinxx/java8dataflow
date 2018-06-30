package yila.experiments.yum;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws Exception {

        AtomicInteger atomicInteger = new AtomicInteger(0);
        Server.start(5005).addInputInterceptor((request) -> atomicInteger.incrementAndGet());
        while (true) {
            System.out.println("Number of request: " + atomicInteger.get());
            Thread.sleep(5000);
        }
    }
}
