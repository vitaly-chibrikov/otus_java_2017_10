package ru.otus.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class NioClient {
    static Logger log = LoggerFactory.getLogger(NioClient.class);

    private int port;
    private String host;

    BlockingQueue<String> queue = new ArrayBlockingQueue<>(16);

    public NioClient(String host, int port) {
        this.port = port;
        this.host = host;
    }

    private void init() throws Exception {
        Selector selector = Selector.open();

        // Create a non-blocking socket channel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        // Kick off connection establishment
        socketChannel.connect(new InetSocketAddress(host, port));


        Thread consoleThread = new Thread(() -> {
            System.out.println("Print command:");
            String line = null;
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    line = scanner.nextLine();
                    queue.add(line);

                    log.info("Sending...");
                    // выставляем channel флаг актиного Write
                    // разблокируем select() на selector
                    socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    selector.wakeup();
                }

            }
        });
        consoleThread.start();

        // Запускаем loop
        loop(selector, socketChannel);
    }

    /**
     * Слушаем события селектора
     */
    private void loop(Selector selector, SocketChannel socketChannel) throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isConnectable()) {
                    log.info("connect: " + key.channel().toString());
                    socketChannel.finishConnect();
                    // теперь в канал можно писать
                    key.interestOps(SelectionKey.OP_WRITE);
                } else if (key.isReadable()) {
                    log.info("read: " + key.channel().toString());
                    buffer.clear();
                    int numRead = socketChannel.read(buffer);
                    if (numRead < 0) {
                        key.cancel();
                        socketChannel.close();
                        return;
                    }
                    byte[] arr = new byte[numRead];
                    buffer.flip();
                    buffer.get(arr);
                    System.out.println("From server: " + new String(arr));
                } else if (key.isWritable()) {
                    log.info("write: " + key.channel().toString());
                    String line = queue.take();
                    if (line != null) {
                        socketChannel.write(ByteBuffer.wrap(line.getBytes()));

                        // Ждем записи в канал
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        NioClient client = new NioClient("localhost", 9000);
        client.init();
    }
}
