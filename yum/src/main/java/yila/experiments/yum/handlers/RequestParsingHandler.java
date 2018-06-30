package yila.experiments.yum.handlers;

import yila.experiments.yum.Request;
import yila.experiments.yum.Server;
import yila.experiments.yum.json.Helpers;
import yila.experiments.yum.json.Pair;

import java.math.BigDecimal;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RequestParsingHandler {

    private static RequestParsingHandler handler;

    public static RequestParsingHandler get() {
        if (handler == null) {
            handler = new RequestParsingHandler();
        }
        return handler;
    }

    public void parse(Server server, AsynchronousSocketChannel channel, String input) {

        CompletableFuture.supplyAsync(
                () -> Request.fromHttp(input, channel)
        ).thenAccept((request) -> {
            server.interceptRequest(request);

            if (request.hasParsingErrors()) {
                request.sendResponse(400, "Error parsing input.");
            } else {
                Optional<RequestHandler> optional = server.match(request);
                optional.ifPresent(requestHandler -> requestHandler.handle(request));

                if (!optional.isPresent()) {
                    request.sendResponse(404);
                }
            }
        });
    }
}
