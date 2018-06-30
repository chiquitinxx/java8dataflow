package yila.experiments.yum.matcher;


import yila.experiments.yum.Action;
import yila.experiments.yum.handlers.RequestHandler;

public class UrlMatcher {

    private final Action action;
    private final String path;
    private final RequestHandler requestHandler;

    public UrlMatcher(Action action, String path, RequestHandler requestHandler) {
        this.action = action;
        this.path = path;
        this.requestHandler = requestHandler;
    }

    public Action getAction() {
        return action;
    }

    public String getPath() {
        return path;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }
}
