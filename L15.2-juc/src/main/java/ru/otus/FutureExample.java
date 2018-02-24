package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class FutureExample {

    public static final Logger log = LoggerFactory.getLogger(FutureExample.class);

    static void sleepSafe(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //
        }
    }

    public static void main(String[] args) throws Exception {

        log.info("Fixed Thread Pool");
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(2, new SimplePool.CustomThreadFactory(false));


        List<Future<Integer>> r = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Future<Integer> future = executorService.submit(() -> {
                int callResult = counter.incrementAndGet();
                sleepSafe(500);
                log.info("completed with: " + callResult);
                return callResult;
            });
            r.add(future);
        }

        // block here
        for (Future<Integer> future : r) {
            Integer integer = future.get();
        }
        log.info("All futures are completed");


        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        log.info("Fixed Thread Pool terminated: " + terminated);

    }
}
