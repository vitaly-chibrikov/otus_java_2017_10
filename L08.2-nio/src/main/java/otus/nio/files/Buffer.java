package otus.nio.files;

import java.nio.ByteBuffer;

/**
 *
 */
public class Buffer {

    public static void main(String[] args) {
        //ByteBuffer buf = ByteBuffer.allocateDirect(32);
        ByteBuffer buf = ByteBuffer.allocate(32);
        buf.putLong(Long.MAX_VALUE);

        System.out.printf("buffer: size=%d, position=%d, limit=%d\n", buf.capacity(), buf.position(), buf.limit());
        buf.putLong(Long.MIN_VALUE);
        System.out.printf("buffer: size=%d, position=%d, limit=%d\n", buf.capacity(), buf.position(), buf.limit());

        buf.flip();
        System.out.printf("buffer: size=%d, position=%d, limit=%d\n", buf.capacity(), buf.position(), buf.limit());

        System.out.println(buf.getLong());
        buf.mark(); // marked
        System.out.println(buf.getLong());
        buf.reset(); // go to mark
        System.out.printf("buffer: size=%d, position=%d, limit=%d\n", buf.capacity(), buf.position(), buf.limit());
        System.out.println(buf.getLong());

        buf.rewind(); // remove mark, set position = 0
        System.out.printf("buffer: size=%d, position=%d, limit=%d\n", buf.capacity(), buf.position(), buf.limit());

    }
}
