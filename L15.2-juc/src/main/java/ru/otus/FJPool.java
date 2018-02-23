package ru.otus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class FJPool {

    public static final Logger log = LoggerFactory.getLogger(FJPool.class);

    static List<Integer> data = Arrays.asList(1, 2, 3);

    public static void main(String[] args) throws Exception {

        ExecutorService pool = ForkJoinPool.commonPool(); //new ForkJoinPool(3);
        for (int i = 0; i < 10; i++) {
            pool.submit(() -> {
                log.info("R ");
            });
        }

//        data.parallelStream().forEach((e) -> log.info("Stream " + e));

        pool.awaitTermination(10, TimeUnit.SECONDS);


        int[] arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }

        Integer invoke = ForkJoinPool.commonPool().invoke(new CustomRecursiveTask(arr));
        System.out.println("R: " + invoke);

    }



    static class CustomRecursiveTask extends RecursiveTask<Integer> {
        private int[] arr;

        private static final int THRESHOLD = 20;

        public CustomRecursiveTask(int[] arr) {
            this.arr = arr;
        }

        @Override
        protected Integer compute() {
            if (arr.length > THRESHOLD) {
                return ForkJoinTask.invokeAll(createSubtasks())
                        .stream()
                        .mapToInt(ForkJoinTask::join)
                        .sum();
            } else {
                return processing(arr);
            }
        }

        private Collection<CustomRecursiveTask> createSubtasks() {
            List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
            dividedTasks.add(new CustomRecursiveTask(
                    Arrays.copyOfRange(arr, 0, arr.length / 2)));
            dividedTasks.add(new CustomRecursiveTask(
                    Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
            return dividedTasks;
        }

        private Integer processing(int[] arr) {
            return Arrays.stream(arr)
                    .filter(a -> a % 2 == 0)
                    .sum();
        }
    }

}
