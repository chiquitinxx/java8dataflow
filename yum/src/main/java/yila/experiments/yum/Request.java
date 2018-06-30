package yila.experiments.yum;

import yila.experiments.yum.handlers.ResponseHttpHandler;
import yila.experiments.yum.json.Helpers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Request {

    private static final int RESPONSE_TIMEOUT = 30;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final Action action;
    private final String url;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final AsynchronousSocketChannel channel;
    private boolean errorParsing = false;

    private Request(Action action, String url,
                    Map<String, String> headers, Map<String, String> params,
                    AsynchronousSocketChannel channel) {
        this.action = action;
        this.url = url;
        this.headers = headers;
        this.params = params;
        this.channel = channel;
    }

    public static Request fromHttp(String http, AsynchronousSocketChannel channel) {
        //TODO generate request from http
        String[] lines = http.split(LINE_SEPARATOR);
        Action action = parseAction(lines);
        String url = parseUrl(lines);
        Map<String, String> headers = parseHeaders(lines);
        Map<String, String> params = parseParams(lines);
        return new Request(action, url, headers, params, channel);
    }

    public boolean hasParsingErrors() {
        return errorParsing;
    }

    public void sendResponse(int status) {
        sendResponse(status, "");
    }

    public void sendResponse(int status, String message) {
        sendResponse(status, message, false);
    }

    public void sendResponse(int status, String message, boolean isJson) {
        String fullMessage = getResponseHeaders(status, message, isJson) +
                message;

        sendMessage(fullMessage);
    }

    public void sendResponse(int status, Jsonnable jsonnable) {
        String message = jsonnable.toJson();
        sendResponse(status, message, true);
    }

    public void sendResponse(int status, Map<String, Object> properties) {
        sendResponse(status, Helpers.fromMap(properties));
    }

    public Action getAction() {
        return action;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    private String getResponseHeaders(int status, String message, boolean isJson) {
        return "HTTP/1.1 " + status + " " + getStatusDescription(status) + "\r\n" +
                "Content-Type: " + (isJson ? "application/json; charset=UTF-8" : "text/plain") +"\r\n" +
                "Server: Yum\r\nContent-Length: " + message.length() + "\r\nConnection: close\r\n\r\n";
    }

    private String getStatusDescription(int status) {
        if (status == 200) return "OK";
        if (status == 400) return "Bad Request";
        if (status == 404) return "Not Found";
        return "OK";
    }

    private void sendMessage(String message) {
        ByteBuffer buffer = ByteBuffer.allocate(message.length());
        buffer.put(message.getBytes());
        buffer.flip();
        channel.write(buffer, RESPONSE_TIMEOUT, TimeUnit.SECONDS, null, new ResponseHttpHandler(channel));
    }

    private static Action parseAction(String[] lines) {
        return Action.valueOf(lines[0].split(" ")[0].toUpperCase());
    }

    private static String parseUrl(String[] lines) {
        return lines[0].split(" ")[1];
    }

    private static Map<String, String> parseHeaders(String[] lines) {
        Map<String, String> result = new HashMap<>();
        if (lines.length > 1) {
            int i = 1;
            String line = lines[i++];
            while (line != null && !"".equals(line.trim()) && i <= lines.length) {
                int pos = line.indexOf(":");
                result.put(line.substring(0, pos).trim(), line.substring(pos + 1).trim());
                if (i < lines.length) {
                    line = lines[i++];
                } else {
                    line = null;
                }
            }
        }
        return result;
    }

    private static Map<String, String> parseParams(String[] lines) {
        int i = 0;
        Map<String, String> result = new HashMap<>();
        String line = lines[i++];
        while (line != null && !isEmptyLine(line) && i < lines.length) {
            line = lines[i++];
        }
        if (isEmptyLine(line) && i < lines.length) {
            String lineParams = lines[i];
            if (lineParams != null && !"".equals(lineParams.trim())) {
                String[] params = lineParams.trim().split("&");
                int j;
                for (j = 0; j < params.length; j++) {
                    String[] values = params[j].split("=");
                    result.put(values[0].trim(), values[1].trim());
                }
            }
        }
        return result;
    }

    private static boolean isEmptyLine(String line) {
        return line != null && (
                "".equals(line.trim()) ||
                        LINE_SEPARATOR.equals(line.trim()) ||
                        "\r".equals(line.trim()) ||
                        "\n".equals(line.trim())
                );
    }
}
