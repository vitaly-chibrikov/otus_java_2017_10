package otus.nio.files;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 100 Mb
 * $ dd if=/dev/zero of=test.img bs=1024 count=0 seek=$[1024*100]
 *
 *
 * Смотрим системные вызовы
 *
 * (LINUS - https://linux.die.net/man/1/strace)
 *
 #!/bin/bash
 java -Xmx16m $1 &
 echo $!
 dtruss -p $! > allout.txt 2>&1
 *
 */
public class FileNIO {

    public static void main(String[] args) throws Exception {
//        fileChannel_write();
//        fileChannel_async();


//        fileChannel_transfer();
//        fileChannel_mmap();

    }


    /**
     * Запись/Чтение в файл
     */
    static void fileChannel_write() throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("data.bin", "rw");
        FileChannel channel = aFile.getChannel();
        final String stub = "deadbeaf";


        // WRITE
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.put(stub.getBytes());

        buf.flip();
        while (buf.hasRemaining()) {
            channel.write(buf);
        }
        channel.close();

        // READ
        RandomAccessFile inFile = new RandomAccessFile("data.bin", "rw");
        FileChannel inChannel = inFile.getChannel();
        ByteBuffer inBuf = ByteBuffer.allocate(48);
        int nRead = inChannel.read(inBuf);
        inBuf.flip();

        byte[] array = new byte[stub.length()];
        inBuf.get(array, 0, stub.length());
        inChannel.close();

        System.out.printf("nRead: %d, data: %s\n", nRead, new String(array));

    }

    // zero-copy transfer over kernel (man sendfile)
    static void fileChannel_transfer() throws Exception {
        RandomAccessFile inFile = new RandomAccessFile("nio.jpg", "r");
        RandomAccessFile outFile = new RandomAccessFile("nio_out.jpg", "rw");

        FileChannel inChannel = inFile.getChannel();
        FileChannel outChannel = outFile.getChannel();

        // transfer without memory buffer (Using DMA)
        inChannel.transferTo(0, inFile.length(), outChannel);
        inChannel.close();
        outChannel.close();
    }

    /*
    Можно зарегистрировать операцию и получить результат асинхронно
     */
    static void fileChannel_async() throws Exception {
        Path path = Paths.get("nio.jpg");

        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 100);

        final long startMs = System.nanoTime();
        Future<Integer> readFuture = fileChannel.read(buffer, 0);
        Integer nRead = readFuture.get();
        System.out.printf("Read %d bytes in %d ms\n", nRead, TimeUnit.MILLISECONDS.convert(System.nanoTime() - startMs, TimeUnit.NANOSECONDS));


        buffer.clear();
        fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("CompletionHandler read: " + result);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("Error");
                exc.printStackTrace();
            }
        });


        // Ждем результата, он в другом потоке
        Thread.sleep(1000);
        fileChannel.close();

    }

    // Using memory-mapped file
    static void fileChannel_mmap() throws Exception {
        int count = 10;

        // RandomAccessFile f = new RandomAccessFile("/dev/shm/cache", "rw");

        RandomAccessFile file = new RandomAccessFile("data.bin", "rw");
        MappedByteBuffer out = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, count);

        for (int i = 0; i < count; i++) {
            System.out.print((char) out.get(i));
        }
    }





}
