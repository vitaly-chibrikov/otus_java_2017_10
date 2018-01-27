package ru.otus.sock;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 */
public class Server {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8091);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        byte[] buf = new byte[256];
        int nRead = inputStream.read(buf);
        if (nRead != -1) {
            String line = new String(buf, 0, nRead);
            System.out.println("client: " + line);
            socket.close();
        }




    }
}
