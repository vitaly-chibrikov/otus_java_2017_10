import java.io.PrintStream;
import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 *
 * -XX:-UseCompressedOops
 *
 *
 * {@link Unsafe sun.misc.Unsafe}
 *
 * get offset of a non-static field in the object in bytes
 * {@link Unsafe#objectFieldOffset(Field)}
 *
 * get offset of a first element in the array
 * {@link Unsafe#arrayBaseOffset(Class)}
 *
 * get size of an element in the array
 * {@link Unsafe#arrayIndexScale(Class)}
 *
 * get address size for your JVM
 * {@link Unsafe#addressSize()}
 */
public class UnsafeUtil {
    public static Unsafe unsafe;
    private static Object[] objArray;

    // офсет первого элемента в массиве
    private static long BASE_OFFSET;

    // размер адреса (ссылки)
    public static int ADDRESS_SIZE;

    // размер хэдера объекта
    public static int HEADER_SIZE;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to get unsafe", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to get unsafe", e);
        }


        ADDRESS_SIZE = unsafe.addressSize();
        HEADER_SIZE = getHeaderSize();
        BASE_OFFSET = unsafe.arrayBaseOffset(Object[].class);

        objArray = new Object[1];
        System.out.println(String.format("BASE_OFFSET: %d, ADDRESS_SIZE: %d, HEADER_SIZE: %d\n", BASE_OFFSET, ADDRESS_SIZE, HEADER_SIZE));
    }

    static class HeaderStub {
        public boolean stub;
    }

    // TODO: как получить размер хэдера?
    private static int getHeaderSize() {
        int headerSize;
        try {
            long off1 = unsafe.objectFieldOffset(HeaderStub.class.getField("stub"));
            headerSize = (int) off1;
        } catch (NoSuchFieldException e) {
            headerSize = -1;
        }
        return headerSize;
    }

    // Нужна ссылка на хипе, вернет значение адреса в памяти
    public static long addressOf(Object obj) {
        objArray[0] = obj;
        return unsafe.getLong(objArray, BASE_OFFSET);
    }

    public static int sizeOfType(Class<?> type) {
        if (type == byte.class) {
            return 1;
        }
        if (type == boolean.class) {
            return 1;
        }
        if (type == short.class) {
            return 2;
        }
        if (type == char.class) {
            return 2;
        }
        if (type == int.class) {
            return 4;
        }
        if (type == float.class) {
            return 4;
        }
        if (type == long.class) {
            return 8;
        }
        if (type == double.class) {
            return 8;
        }
        return 8; // reference size
    }

    public static void dump(PrintStream ps, long address, long size) {
        System.out.println(String.format("[%02x]: %d\tbyte_dump", address, size));
        for (int i = 0; i < size; i++) {
            if (i % 16 == 0) {
                ps.print(String.format("[0x%04x]: ", i));
            }
            ps.print(String.format("%02x %s", unsafe.getByte(address + i), ((i + 1) % 4 == 0) ? " " : ""));
            if ((i + 1) % 16 == 0) {
                ps.println();
            }
        }
        ps.println("\n");
    }

    public static void dumpChar(PrintStream ps, long address, long size) {
        System.out.println(String.format("\n[%02x]: %d\tchar_dump", address, size));
        for (int i = 0; i < size; i += 2) {
            if (i % 8 == 0) {
                ps.print(String.format("[0x%04x]: ", i));
            }
            ps.print(String.format("%s ", unsafe.getChar(address + i)));
            if ((i + 2) % 8 == 0) {
                ps.println();
            }
        }
        ps.println();
    }

    public static void dumpString(String str) {
        /**
         * Доступ к массиву под String
         */
        try {
            Field valueField = String.class.getDeclaredField("value");
            valueField.setAccessible(true);
            Object stringInternalArray = valueField.get(str);
            long address = UnsafeUtil.addressOf(stringInternalArray);
            dumpChar(System.out, address + HEADER_SIZE, 32);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dumpObject(PrintStream ps, Object object) throws Exception {
        ps.println("dumpObject: " + object.getClass());
        final long address = addressOf(object);
        dumpHeader(ps, object);
        Class clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            long offset = unsafe.objectFieldOffset(field);
            long size = sizeOfType(field.getType());
            ps.printf("[%02x+%d]: %d\t%s\n", address, offset, size, field);
            if (field.getType() == String.class) {
                System.out.println(String .format("Ref: %02x", unsafe.getLong(address + offset)));
                dumpString((String) field.get(object));
            } else {
                dump(ps, address + offset, size);
            }
        }
    }

    public static void dumpHeader(PrintStream ps, Object object) {
        ps.println("dumpHeader: " + object.getClass());
        long address = addressOf(object);
        dump(ps, address, HEADER_SIZE);
    }

    public static void main(String[] args) throws Exception {
        T t = new T();
        long address = UnsafeUtil.addressOf(t);

        /*
         * Дамп объекта по адресу
         */
        dump(System.out, address, 120);
        dumpObject(System.out, t);


        /*
         * Пустой Object
         * хеш-код объекта должен оставаться постоянным в течение жизни приложения.
         *
         * Встроенный (identity) хеш-код генерируется лишь один раз для каждого объекта при первом вызове метода hashCode()
         */
        System.out.printf("hashCode() 0x%04x\n", t.hashCode());
//        System.out.printf("identityHashCode():\t0x%04x\n", System.identityHashCode(t));

        dumpHeader(System.out, t);
    }

    static class T {
        int a1 = 15;
        int a2 = 255;
        String val = "aaaaaa";

        @Override
        public int hashCode() {
            return 0xffffffff;
        }
    }

}
