package ru.otus.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

import org.h2.tools.Server;

/**
 *
 */
public class Main {

    static ThreadLocalRandom rnd = ThreadLocalRandom.current();

    public static void main(String[] args) throws Exception {
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        Thread.sleep(1000);


        final ConnectionPool pool = new QueueConnectionPool("jdbc:h2:~/test", "sa", "", "org.h2.Driver", 1);

        Runnable r = () -> {
            while (true) {
                try {
                    doConnect(pool);
                    Thread.sleep(rnd.nextInt(1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(r).start();
        }


    }

    static void doConnect(ConnectionPool pool) {
        try (Connection connection = pool.get()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");
            while (resultSet.next()) {
                //System.out.println("(" + resultSet.getString(1) + ", " + resultSet.getString(2) + ")");
            }
            Thread.sleep(rnd.nextInt(10_000));
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }
}
