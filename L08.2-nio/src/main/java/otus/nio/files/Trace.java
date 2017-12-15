package otus.nio.files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**
 * OSX:
 * $ sudo dtruss -t open_nocancel -p <pid>
 * <p>
 * Linux:
 * <p>
 * $ sudo strace -F -p <pid> 2>&1| grep -v clock_gettime | grep -v futex
 */
public class Trace {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        final String stub = "deadbeaf";
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            switch (line) {
                case "nio_write":
                    writeNio(stub.getBytes());
                    break;
                case "io_write":
                    writeIo(stub.getBytes());
                    break;
                case "nio_write_direct":
                    writeNioDirect(stub.getBytes());
                    break;
                case "io_copy":
                    copyIo();
                    break;
                case "nio_copy":
                    copyNio();
                    break;
                case "mmap_write":
                    mmap_write();
                    break;
                case "mmap_read":
                    mmap_read();
                    break;
                default:
                    System.out.println("no command: " + line);
            }
        }
    }

    static void mmap_read() throws Exception {
        RandomAccessFile file = new RandomAccessFile("/dev/shm/cache", "rw");
        MappedByteBuffer in = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        byte[] arr = new byte[128];
        in.get(arr);
        System.out.println("mmap: " + new String(arr));
        file.close();
    }

    static void mmap_write() throws Exception {
        RandomAccessFile file = new RandomAccessFile("/dev/shm/cache", "rw");
        MappedByteBuffer out = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 100);
        String stub = "deadbeaf";
        out.put(stub.getBytes());
        file.close();
    }

    static void writeNio(final byte[] data) throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("data.bin", "rw");
        FileChannel channel = aFile.getChannel();


        // WRITE
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.put(data);

        buf.flip();
        while (buf.hasRemaining()) {
            channel.write(buf);
        }
        channel.close();
    }

    static void writeNioDirect(final byte[] data) throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("data.bin", "rw");
        FileChannel channel = aFile.getChannel();


        // WRITE
        ByteBuffer buf = ByteBuffer.allocateDirect(48);
        buf.put(data);

        buf.flip();
        while (buf.hasRemaining()) {
            channel.write(buf);
        }
        channel.close();
    }

    static void copyNio() throws Exception {
        RandomAccessFile inFile = new RandomAccessFile("Trace.java", "r");
        RandomAccessFile outFile = new RandomAccessFile("Trace_.java", "rw");

        FileChannel inChannel = inFile.getChannel();
        FileChannel outChannel = outFile.getChannel();

        // transfer without memory buffer (Using DMA)
        inChannel.transferTo(0, inFile.length(), outChannel);
        inChannel.close();
        outChannel.close();
    }

    static void copyIo() throws Exception {
        File outFile = new File("Trace_.java");
        InputStream in = new FileInputStream(new File("Trace.java"));
        OutputStream out = new FileOutputStream(outFile);

        byte[] arr = new byte[1024];
        int nRead = in.read(arr);
        int pos = 0;
        while (nRead != -1) {
            out.write(arr);
            pos = nRead;
            nRead = in.read(arr);
        }
        in.close();
        out.close();

    }

    static void writeIo(final byte[] data) throws Exception {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("data.bin"));
        out.write(data);
        out.close();
    }
}
