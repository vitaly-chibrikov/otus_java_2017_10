package ru.otus.pool_lib;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Created by tully.
 */
public class HikariCPDataSourceFactory implements DataSourceFactory {
    private HikariDataSource hikariDataSource;

    @Override
    public DataSource get() {
        if (hikariDataSource != null) {
            return hikariDataSource;
        }

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:h2:~/test");
        config.setUsername("sa");
        config.setPassword("");

        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(0);
        config.setValidationTimeout(1000);
        config.setAutoCommit(false);
        config.setRegisterMbeans(true);

        hikariDataSource = new HikariDataSource(config);
        return hikariDataSource;
    }

}
