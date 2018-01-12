package ru.otus.pool_lib;

import java.beans.PropertyVetoException;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Created by tully.
 */
public class C3P0DataSourceFactory implements DataSourceFactory {

    private ComboPooledDataSource cpds;


    @Override
    public DataSource get() throws PropertyVetoException {
        if (cpds != null) {
            return cpds;
        }

        this.cpds = new com.mchange.v2.c3p0.ComboPooledDataSource();

        cpds.setDriverClass("org.h2.Driver");
        cpds.setJdbcUrl("jdbc:h2:~/test");
        cpds.setUser("sa");
        cpds.setPassword("");

        cpds.setInitialPoolSize(1);
        cpds.setMinPoolSize(1);
        cpds.setAcquireIncrement(1);
        cpds.setMaxPoolSize(1);

        return cpds;
    }
}
