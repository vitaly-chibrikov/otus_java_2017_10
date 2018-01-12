package ru.otus.pool_lib;

import java.beans.PropertyVetoException;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;

import org.h2.tools.Server;

public class Main {
    public static void main(String[] args) throws PropertyVetoException, SQLException, InterruptedException {

        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        Thread.sleep(1000);





        DataSourceFactory factory = new C3P0DataSourceFactory();
        //DataSourceFactory factory = new DBCPDataSourceFactory();
//        DataSourceFactory factory = new HikariCPDataSourceFactory();

        Connection connection = factory.get().getConnection();
        System.out.println("DatabaseProductName: " + connection.getMetaData().getDatabaseProductName());

        Connection connection2 = factory.get().getConnection();
        System.out.println("DriverName: " + connection2.getMetaData().getDriverName());

        Connection connection3 = factory.get().getConnection();
        System.out.println("URL: " + connection3.getMetaData().getURL());

        hold();
    }


    @SuppressWarnings("InfiniteLoopStatement")
    private static void hold() throws InterruptedException {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        while (true) {
            Thread.sleep(1000);
        }
    }

}
