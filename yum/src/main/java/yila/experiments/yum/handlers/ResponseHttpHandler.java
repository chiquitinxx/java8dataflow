package yila.experiments.yum.handlers;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ResponseHttpHandler implements CompletionHandler<Integer, Object> {

    private final AsynchronousSocketChannel channel;

    public ResponseHttpHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, Object attachment) {
        closeChannel();
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        System.out.println("Failed ResponseHttpHandler " + exc.getMessage());
        closeChannel();
    }

    private void closeChannel() {
        try {
            if (channel.isOpen()) {
                channel.close();
            }
        } catch (Throwable t) {
            //TODO
            System.out.println("Error closing channel:" + t.getMessage());
            t.printStackTrace();
        }
    }
}
