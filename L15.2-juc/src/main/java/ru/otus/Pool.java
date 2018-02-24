package ru.otus;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.istack.internal.NotNull;

/**
 *
 */
public class Pool {

    public static final Logger log = LoggerFactory.getLogger(Pool.class);

    private ServerSocket sSocket;

    void sleepSafe(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //
        }
    }

    public void destroy() {
        if (sSocket != null) {
            try {
                sSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void serve(ExecutorService pool) throws Exception {
        sSocket = new ServerSocket(8080, 10, InetAddress.getByName("localhost"));
        while (true) {
//            sleepSafe(1000);
            pool.execute(() -> {
                try {
                    log.info("Waiting for connection " + Thread.currentThread().getName());
                    Socket accept = sSocket.accept();
                    //
                } catch (Exception e) {
                    log.error("Unexpectedly closed");
                }
            });
        }

    }


    /**
     *
     * 1) ThreadPoolExecutor with CPU usage
     * 2) Change Queue as Q
     * 3) set ArrayBlockingQueue and fail with RejectedExecutionException
     * 4) Write custom RejectedExecutionHandler
     */
    public static void main(String[] args) throws Exception {
//        ExecutorService pool = Executors.newFixedThreadPool(2);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(3));


        log.info("Pool created");

//        pool.setRejectedExecutionHandler((r, executor) -> {
//            System.out.println("Rejected");
//        });


//        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        Pool p = new Pool();
        try {
            p.serve(pool);
        } catch (Exception e) {
            log.error("Failed on serve", e);
            p.destroy();
            pool.shutdownNow();
            pool.awaitTermination(1, TimeUnit.SECONDS);
            log.warn("Pool destroyed");
        }
    }

    static class Q<E> extends LinkedBlockingQueue<E> {
        @Override
        public boolean offer(@NotNull E o) {
            System.out.println("Offering: " + o);
            return super.offer(o);
        }
    }
}
