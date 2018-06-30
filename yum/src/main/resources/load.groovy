import java.util.concurrent.atomic.AtomicInteger

def times = 500
AtomicInteger done = new AtomicInteger(0)

Date before = new Date()
times.times {
    Thread.start {
        new URL("http://localhost:5005/pepe").getText()
        //new URL("http://google.com").getText()
        done.incrementAndGet()
    }

}


while (done.get() < times) {
    println 'Done: '+done.get()
    sleep(1)
}

println 'Done in ' + (new Date().time - before.time) + ' ms.'
