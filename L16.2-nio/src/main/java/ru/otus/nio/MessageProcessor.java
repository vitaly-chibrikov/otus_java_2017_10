package ru.otus.nio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MessageProcessor {
    Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    ExecutorService pool;

    public MessageProcessor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        this.pool = threadPoolExecutor;
    }

    public void process(Message message) throws Exception {
        pool.submit(message);
    }
}
