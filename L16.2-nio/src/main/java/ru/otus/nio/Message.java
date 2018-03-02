package ru.otus.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Message implements Runnable {
    static Logger log = LoggerFactory.getLogger(Message.class);
    SocketChannel channel;
    byte[] data;

    public Message(SocketChannel channel, byte[] data) {
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void run() {
        try {
            log.info("Processing: " + channel.toString());

            String string = "¯\\_(ツ)_/¯" + new String(data);
            channel.write(ByteBuffer.wrap(string.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeChannel() throws Exception {
        log.error("Closing channel");
        channel.close();
    }
}
