package memory;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

/**
 * -XX:-UseCompressedOops -Xmx64m -Xms64m -XX:+PrintGCDetails
 */
public class MemoryTest {

    public static final int SIZE = 100_000;

    public static void main(String[] args) {
        printUsage(String::new, "Empty string");
        printUsage(Object::new, "Empty object");
    }

    /**
     * @param supplier - генерирует значения для заполнения массива.
     * @param comment  - описание теста
     * @return Object чтобы JVM не съела ссылку. Или можно завести массив как поле класса
     */
    private static <T> Object printUsage(@NotNull Supplier<T> supplier, @NotNull String comment) {
        final Object[] objs = new Object[SIZE];
        Runtime runtime = Runtime.getRuntime();

        runtime.gc();
        // TODO: почему меряем usedAfter-usedBefore, а не freeBefore - freeAfter?
        long memBefore = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < SIZE; i++) {
            objs[i] = supplier.get();
        }

        // TODO: пока мы создавали объекты, другие треды тоже могли аллоцировать себе память
        runtime.gc();
        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(String.format("\n%-30s\tSIZEOF(): %d bytes\n", comment, Math.round((double) (memAfter - memBefore) / SIZE)));

        // TODO: Avoid optimization
        return objs;
//        return null;
    }

}
