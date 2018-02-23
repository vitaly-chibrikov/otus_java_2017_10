package ru.otus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SimplePool {
    public static final Logger log = LoggerFactory.getLogger(SimplePool.class);

    static void sleepSafe(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //
        }
    }

    public static void main(String[] args) throws Exception {
//        fixedThreadPool();
        cachedThreadPool();
    }

    private static void cachedThreadPool() throws Exception {
        log.info("Cached Thread Pool");
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 4; i++) {
            executorService.submit(() -> {
                sleepSafe(TimeUnit.SECONDS.toMillis(2));
                log.info("completed");
            });
        }

        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        log.info("Cached Thread Pool terminated: " + terminated);
    }

    private static void fixedThreadPool() throws Exception {
        log.info("Fixed Thread Pool");
        ExecutorService executorService = Executors.newFixedThreadPool(2, new CustomThreadFactory(false));
        for (int i = 0; i < 4; i++) {
            executorService.submit(() -> {
                sleepSafe(TimeUnit.SECONDS.toMillis(2));
                log.info("completed");
            });
        }

//        executorService.shutdown();
//        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
//        log.info("Fixed Thread Pool terminated: " + terminated);
    }

    static class CustomThreadFactory implements ThreadFactory {

        private AtomicLong counter = new AtomicLong(0);
        private boolean isDaemon;

        public CustomThreadFactory(boolean isDaemon) {
            this.counter = counter;
            this.isDaemon = isDaemon;
        }

        @Override
        public Thread newThread(Runnable r) {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            Thread thread = new Thread(threadGroup, r, "Selector-" + counter.getAndIncrement());
            thread.setDaemon(isDaemon);
            return thread;
        }
    }
}
