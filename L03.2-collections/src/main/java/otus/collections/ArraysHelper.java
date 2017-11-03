package otus.collections;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 *
 */
public class ArraysHelper {

    public static void main(String[] args) {
        int[] ints = new int[10];
        Arrays.fill(ints, 42);
        foo(ints);

        boolean[] booleans = new boolean[3];
        Arrays.fill(booleans, true);
        foo(booleans);

    }

    static void foo(Object array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Not an array");
        }
        System.out.println("Length: " + Array.getLength(array));

        for (int i = 0; i < Array.getLength(array); i++) {
            System.out.print(Array.get(array, i) + " ");
        }
        System.out.println();
    }
}
