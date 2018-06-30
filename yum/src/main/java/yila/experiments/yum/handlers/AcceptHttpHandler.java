package yila.experiments.yum.handlers;

import yila.experiments.yum.Server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.TimeUnit;

public class AcceptHttpHandler implements CompletionHandler<AsynchronousSocketChannel,Void> {

    private static final int READ_TIMEOUT = 30;
    private final AsynchronousServerSocketChannel serverChannel;
    private final Server server;

    public AcceptHttpHandler(Server server, AsynchronousServerSocketChannel serverChannel) {
        this.serverChannel = serverChannel;
        this.server = server;
    }

    public void completed(AsynchronousSocketChannel channel, Void attachment) {
        serverChannel.accept(null, this);

        ByteBuffer byteBuffer = ByteBuffer.allocate(400);
        channel.read(byteBuffer, READ_TIMEOUT, TimeUnit.SECONDS, null,
                new ReadHttpHandler(server, channel, byteBuffer));
    }

    public void failed(Throwable exc, Void attachment) {
        //TODO
        //Called even when closing the channel
    }
}
