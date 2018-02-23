package ru.otus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Primitives {
    public static final Logger log = LoggerFactory.getLogger(Primitives.class);

    public static void main(String[] args) {
        _countDownLatch();
    }

    static void _semaphore() {
        Semaphore s = new Semaphore(2);
    }

    static void _countDownLatch() {
        CountDownLatch latch = new CountDownLatch(3);

        Runnable r = () -> {
            latch.countDown();
            log.info("Count down");


            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Continue");

        };

        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();


        try {
            Thread.sleep(1000);
            System.out.println("Start!");
            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
