package ru.otus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class ThreadLocal_ {
    static ThreadLocal<Integer> localCounter = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 10_000; j++) {
                    localCounter.set(j);
                }
                System.out.println(localCounter.get());
            });
        }
    }
}
