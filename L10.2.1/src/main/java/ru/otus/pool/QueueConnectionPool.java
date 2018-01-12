package ru.otus.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class QueueConnectionPool implements ConnectionPool {

    static Logger log = LoggerFactory.getLogger(QueueConnectionPool.class);

    private final String url;
    private final String login;
    private final String pass;
    private final BlockingQueue<PoolConnection> pool;

    private AtomicInteger counter = new AtomicInteger(0);

    public QueueConnectionPool(String url, String login, String pass, String driver, int initConnections) {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.url = url;
        this.login = login;
        this.pass = pass;
        pool = new ArrayBlockingQueue<PoolConnection>(16);
        for (int i = 0; i < initConnections; i++) {
            pool.add(getConnection());
        }
    }

    private PoolConnection getConnection() {
        PoolConnection conn = null;
        try {
            log.info("Create connection");
            return new PoolConnection(DriverManager.getConnection(url, login, pass));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create connection", e);
        }
    }

    @Override
    public Connection get() {
        final PoolConnection poolConnection;
        if (pool.isEmpty()) {
            poolConnection = getConnection();
        } else {
            try {
                poolConnection = pool.take();
                log.info("get() " + poolConnection.id);
                return poolConnection;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }

        return poolConnection;
    }

    @Override
    public void dispose() {

    }

    private class PoolConnection extends ConnectionWrapper {
        private int id;

        public PoolConnection(Connection connection) {
            super(connection);
            id = counter.getAndIncrement();
        }

        @Override
        public void close() throws SQLException {
            pool.add(this);
            log.info("Connection close(): " + pool.size());
        }
    }
}
