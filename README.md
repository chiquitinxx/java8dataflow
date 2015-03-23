Experiments with Java 8
---------------------

Trying to use Dataflows with Java 8 and Lambdas as Groovy does with amazing [GPars](http://gpars.codehaus.org/)

Example using Dataflow variables and task:

    DataflowVariable<Integer> initialDistance = new DataflowVariable<Integer>();
    DataflowVariable<Integer> acceleration = new DataflowVariable<Integer>();
    DataflowVariable<Integer> time = new DataflowVariable<Integer>();
    task(() -> {
        initialDistance.set(100);
        acceleration.set(2);
        time.set(10);
    });

    int result = initialDistance.get() + acceleration.get() / 2 * (time.get() * time.get());
    assertEquals(result, 200);

Example whenAllBound and task:

    String info = "";
    DataflowVariable hello = new DataflowVariable();
    Future world = task(() -> {
        hello.set("Hello");
        return "World";
    });
    whenAllBound((values -> info = values[0] + " - " + values[1]), hello, world);
    Thread.sleep(50);
    assertEquals("Hello - World", info);

Just for fun, welcome your ideas, suggestions, ...