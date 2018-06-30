package yila.experiments.yum;

import yila.experiments.yum.handlers.AcceptHttpHandler;
import yila.experiments.yum.handlers.RequestHandler;
import yila.experiments.yum.matcher.UrlMatcher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static yila.experiments.yum.executors.LambdaExecutor.executeAsync;

public class Server {

    final AsynchronousServerSocketChannel serverChannel;

    private List<Consumer<Request>> inputInterceptorListeners = new CopyOnWriteArrayList<>();
    private List<UrlMatcher> urlMatchers = new CopyOnWriteArrayList<>();

    private Server(int port) {
        try {

            this.serverChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
            AcceptHttpHandler acceptHttpHandler = new AcceptHttpHandler(this, this.serverChannel);
            this.serverChannel.accept(null, acceptHttpHandler);
            System.out.println("Server started at " + port + ".");

        } catch (IOException ioException) {
            throw new RuntimeException("Error initializing server on port " + port
                    + ": " + ioException.getMessage());
        }
    }

    public static Server start(int port) {
        return new Server(port);
    }

    public void stop() {
        try {
            serverChannel.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public Server on(String match, RequestHandler requestHandler) {
        return on(Action.GET, match, requestHandler);
    }

    public Server on(Action action, String match, RequestHandler requestHandler) {
        urlMatchers.add(new UrlMatcher(action, match, requestHandler));
        return this;
    }

    public Server addInputInterceptor(Consumer<Request> consumer) {
        inputInterceptorListeners.add(consumer);
        return this;
    }

    public void interceptRequest(Request request) {
        if (inputInterceptorListeners.size() > 0) {
            inputInterceptorListeners.forEach(
                    (consumer) -> executeAsync(() -> consumer.accept(request))
            );
        }
    }

    public Optional<RequestHandler> match(Request request) {
        Optional<UrlMatcher> firstPair = urlMatchers.stream()
                .filter((urlMatcher) -> matchUrl(urlMatcher, request)).findFirst();
        return Optional.ofNullable(firstPair.map(UrlMatcher::getRequestHandler).orElse(null));
    }

    private boolean matchUrl(UrlMatcher urlMatcher, Request request) {
        return urlMatcher.getAction().equals(request.getAction())
                && urlMatcher.getPath().equals(request.getUrl());
    }
}
