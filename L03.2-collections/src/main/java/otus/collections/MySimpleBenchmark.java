package otus.collections;

import java.util.Collection;
import java.util.HashSet;

/**
 * {@link System#nanoTime()} - особенности System.nanoTime()
 *
 * <pre>
 *     jlong os::javaTimeNanos() {
         if (Linux::supports_monotonic_clock()) {
             struct timespec tp;
             int status = Linux::clock_gettime(CLOCK_MONOTONIC, tp);
             assert(status == 0, "gettime error");
             jlong result = jlong(tp.tv_sec) * (1000 * 1000 * 1000) + jlong(tp.tv_nsec);
             return result;
         }
 }
 * </pre>
 *
 * $man clock_gettime
 *
 * CLOCK_MONOTONIC    clock that increments monotonically,
 * tracking the time since an arbitrary point, and will continue to increment while the system is asleep.
 *
 *
 *
 * {@link System#currentTimeMillis()}
 * $man  gettimeofday
 *
 * The time is
 * expressed in seconds and microseconds since midnight (0 hour), January 1, 1970.
 * The resolution of the system clock is hardware dependent
 *
 */
public class MySimpleBenchmark {

    private static final int MEASURE_COUNT = 100;

    public static void main(String... args) {
        Collection<Integer> example = new HashSet<>();
        int min = 0;
        int max = 9_999_999;
        for (int i = min; i < max + 1; i++) {
            example.add(i);
        }


        calcTime(() -> example.contains(0));
    }

    private static void calcTime(Runnable runnable) {
        // 1) TODO: разница между currentTimeMillis/nanoTime
        // 2) TODO: что здесь меряем?
        long startTime = System.nanoTime();
        for (int i = 0; i < MEASURE_COUNT; i++)
            runnable.run();
        long finishTime = System.nanoTime();
        long timeNs = (finishTime - startTime) / MEASURE_COUNT;
        System.out.println("Time spent: " + timeNs + "ns (" + timeNs / 1_000_000 + "ms)");
    }
}

