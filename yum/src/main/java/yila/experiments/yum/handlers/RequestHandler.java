package yila.experiments.yum.handlers;

import yila.experiments.yum.Request;

@FunctionalInterface
public interface RequestHandler {

    void handle(Request request);
}
