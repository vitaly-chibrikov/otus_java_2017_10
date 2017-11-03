package otus.collections.jmh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMH Home:
 * http://openjdk.java.net/projects/code-tools/jmh/
 *
 * See pom.xml for dependencies
 *
 *
 *
 */
@BenchmarkMode(value = { Mode.AverageTime, Mode.SingleShotTime })
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class JMH_Collections {

    public static final int SIZE = 1000;

    ArrayList<Integer> arrayList;
    LinkedList<Integer> linkedList;
    HashSet<Integer> hashSet;

    @Setup
    public void prepare() {
        arrayList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            arrayList.add(i);
        }
        hashSet = new HashSet<>(arrayList);
        linkedList = new LinkedList<>(arrayList);
    }

    @Benchmark
    public Object testArrayListContainsFirst() {
        return arrayList.contains(0);
    }

    @Benchmark
    public Object testArrayListContainsLast() {
        return arrayList.contains(SIZE - 1);
    }

    @Benchmark
    public Object testLinkedListContainsFirst() {
        return linkedList.contains(0);
    }

    @Benchmark
    public Object testLinkedListContainsLast() {
        return linkedList.contains(SIZE - 1);
    }

    @Benchmark
    public Object testHashSetContainsLast() throws Exception {
        return hashSet.contains(SIZE - 1);
    }

    @Benchmark
    public Object testHashSetContainsFirst() throws Exception {
        return hashSet.contains(0);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_Collections.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
