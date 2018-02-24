package ru.otus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class CompletFuture {

    public static final Logger log = LoggerFactory.getLogger(CompletFuture.class);

    static class ApiItem {
        String name;
        int val;

        public ApiItem(String name, int val) {
            this.name = name;
            this.val = val;
        }

        @Override
        public String toString() {
            return "ApiItem{" +
                    "name='" + name + '\'' +
                    ", val=" + val +
                    '}';
        }
    }

    //     load item from db/network
    static ApiItem loadItem() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }
        return new ApiItem("A", 1);
    }

    static void processItem(ApiItem item) {
        System.out.println("Processing item: " + item);
        ;
    }

    static void _1() throws Exception {
        // Простое действие выполнится асинхронно
        // Можно задать свой пул для исполнения
        CompletableFuture.supplyAsync(() -> "Result");


        CompletableFuture
                .supplyAsync(() -> {
                    return loadItem();
                })
//                .thenApply(new Function<ApiItem, ApiItem>() {
//                    @Override
//                    public ApiItem apply(ApiItem item) {
//                        item.val += 100;
//                        return item;
//                    }
//                })
                .thenAccept(new Consumer<ApiItem>() {
                    @Override
                    public void accept(ApiItem apiItem) {
                        processItem(apiItem);
                    }
                });


//        CompletableFuture<ApiItem> completableFuture = CompletableFuture.supplyAsync(CompletFuture::loadItem);
//        completableFuture.thenAccept(CompletFuture::processItem);


        // Waiting for all tasks in default pool
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);

    }

    static void _2() throws Exception {
        CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });
        CompletableFuture<Integer> c2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        });
        CompletableFuture<Integer> c3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 3;
        });

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(c1, c2, c3);
        System.out.println("AnyOf: " + anyOf.get());

    }

    static void _3() throws Exception {
        CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (true) {
                throw new IllegalStateException();
            }
            return 1;
        });

        CompletableFuture<Integer> exceptionally = c1.exceptionally(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) {
                log.info("Failed on computation", throwable);
                return Integer.MIN_VALUE;
            }
        });

        exceptionally.thenAccept((it) -> System.out.println("Completed: " + it));


    }


    public static void main(String[] args) throws Exception {
//        _1();
//        _2();
        _3();

        // Waiting for all tasks in default pool
        ForkJoinPool.commonPool().awaitTermination(10, TimeUnit.SECONDS);
    }
}
