package otus.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openjdk.jol.info.GraphLayout;

/**
 *
 */
public class MemoryUsage {

    public static final int SIZE = 1000;

    public static void main(String[] args) {
        long[] arr = new long[SIZE];
        Arrays.fill(arr, 42);
        printUsage(arr);

        List<Long> src = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            src.add((long)i);
        }

        printUsage(src);
        printUsage(CollectionHelper.collectionToArray(src));
        printUsage(new LinkedList<>(src));
        printUsage(new HashSet<>(src));

        // генерим мапу Long->Long
        printUsage(src.stream().collect(Collectors.toMap(Function.identity(), Function.identity())));

        // TODO: размер set/map


        printUsage(new TreeSet<>(src));
    }

    static void printUsage(Object obj) {
        long total = GraphLayout.parseInstance(obj).totalSize();
        long perItem = Math.round((double) (total) / SIZE);
        System.out.println(String.format("%-30s: %-6d bytes\t(%-3d b/item)", obj.getClass(), total, perItem));
    }
}
