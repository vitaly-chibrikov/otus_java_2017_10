package ru.otus.sock;

import java.io.OutputStream;
import java.net.Socket;

/**
 *
 */
public class Client {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting client...");
        Socket socket = new Socket("localhost", 8091);

        OutputStream outputStream = socket.getOutputStream();

        outputStream.write("Hello".getBytes());
        outputStream.flush();
        socket.close();
    }
}
