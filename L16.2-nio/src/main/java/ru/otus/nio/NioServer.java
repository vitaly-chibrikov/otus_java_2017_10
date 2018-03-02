package ru.otus.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class NioServer {
    static Logger log = LoggerFactory.getLogger(NioServer.class);

    private Selector selector;
    private MessageProcessor processor;
    final ByteBuffer buffer = ByteBuffer.allocate(256);

    public NioServer(int port) throws IOException {
        // Создаем неблокирующий channel и привязываем его к порту сервера
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));

        // Мультиплексор для channel'a
        selector = Selector.open();

        // Селектор будет отлавливать события определенного типа на этом channel
        // При инициализации нужно отлавливать события - "Подключение клиента"
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        processor = new MessageProcessor();
    }

    private void loop() {
        while (true) {
            try {
                // Ждем событий на channel
                selector.select();

                // Может прийти несколько событий одновременно, для каждого события сохраняется ключ
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    // Получаем и сразу удаляем ключ - мы его обработали
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    // В зависимости от события вызываем обработчик
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        log.info("READ: " + key.channel().toString());
                        read(key);
                    } else if (key.isWritable()) {
                        log.info("WRITE: " + key.channel().toString());
                        write(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        // Если пришел accept, значит используется ServerSocketChannel
        // на accept() можно получить канал к клиенту (по аналогии с Socket)
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        log.info("ACCEPT: " + socketChannel.toString());
        socketChannel.configureBlocking(false);
        // Регистрируем на селекторе новый канал, сервер пассивный - ждет, когда клиент напишет
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read_(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int numRead;
        try {
            buffer.clear();
            numRead = socketChannel.read(buffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.cancel();
            socketChannel.close();
            return;
        }
        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            key.channel().close();
            key.cancel();
            return;
        }
        buffer.flip();
        byte[] data = new byte[numRead];
        buffer.get(data);
        processor.process(new Message(socketChannel, data));
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(32);

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(buffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.cancel();
            socketChannel.close();
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            key.channel().close();
            key.cancel();
            return;
        }

        // Read data from channel and echoing
        buffer.flip();
        byte[] data = new byte[numRead];
        buffer.get(data);
        socketChannel.write(ByteBuffer.wrap(data));
        System.out.println("on read: " + new String(data));
        // Ждем записи в канал
//        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String resp = "deadbeaf";
        ByteBuffer buffer = ByteBuffer.wrap(resp.getBytes());
        socketChannel.write(buffer);

//        key.interestOps(SelectionKey.OP_READ);
    }

    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer(9000);
        server.loop();
    }
}
