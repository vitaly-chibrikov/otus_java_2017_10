package ru.otus.pool_lib;


import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Created by tully.
 */
public interface DataSourceFactory {
    DataSource get() throws PropertyVetoException;
}
