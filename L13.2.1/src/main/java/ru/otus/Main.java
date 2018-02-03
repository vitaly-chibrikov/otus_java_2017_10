package ru.otus;

import java.lang.management.ManagementFactory;
import java.util.Scanner;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.mbean.Log4JManager;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static final String OBJECT_NAME = "ru.otus.log.jmx:type=JMX Manager";

    private static void initMBean() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName(OBJECT_NAME);
        Log4JManager mbean = new Log4JManager();
        mbs.registerMBean(mbean, name);
    }

    public static void main(String[] args) throws Exception {
        initMBean();
        testMbean();
    }

    static void testMbean() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            log.debug("_DEBUG_");
            log.info("_INFO_");
            log.error("_ERROR_");
        }
    }
}
