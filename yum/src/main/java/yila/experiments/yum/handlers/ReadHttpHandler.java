package yila.experiments.yum.handlers;

import yila.experiments.yum.Server;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadHttpHandler implements CompletionHandler<Integer, Object> {

    private final AsynchronousSocketChannel channel;
    private final ByteBuffer byteBuffer;
    private final Server server;

    public ReadHttpHandler(Server server, AsynchronousSocketChannel channel, ByteBuffer byteBuffer) {
        this.channel = channel;
        this.byteBuffer = byteBuffer;
        this.server = server;
    }

    public void completed(Integer result, Object attachment) {
        if (result > 0) {
            byteBuffer.flip();
            String input = new String(byteBuffer.array()).substring(0, result);
            RequestParsingHandler.get().parse(server, channel, input);
        }
    }

    public void failed(Throwable exc, Object attachment) {
        //TODO
        exc.printStackTrace();
    }
}
