package ru.otus.mbean;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class Log4JManager implements Log4JManagerMBean {

    @Override
    public void setDebugLevel() {
        Configurator.setLevel("ru.otus", Level.DEBUG);
    }

}
