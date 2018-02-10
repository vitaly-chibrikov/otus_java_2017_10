package ru.otus;

import org.eclipse.jetty.server.Server;

/**
 *
 */
public class ServerLogging {

    public static void main(String[] args)throws Exception {
        Server server = new Server(8081);
        server.start();
        server.join();
    }
}
