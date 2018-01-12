package ru.otus.pool;

import java.sql.Connection;

/**
 *
 */
public interface ConnectionPool {

    Connection get();

    void dispose();
}
