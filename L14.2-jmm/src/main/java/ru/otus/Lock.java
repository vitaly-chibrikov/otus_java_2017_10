package ru.otus;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public interface Lock {
    void lock();
    void unlock();

// -----------// -----------// -----------// ----------- //


    Logger log = LoggerFactory.getLogger(Lock.class);

    static void main(String[] args) throws Exception {
        Lock lock = new MyLock();
        Counter c = new Counter(lock);

        Runnable r = () -> {
            for (int i = 0; i < 10_000; i++) {
                c.inc();
            }
        };

        final int nThread = 10;
        log.info("Starting threads: " + nThread);
        Thread[] ts = new Thread[nThread];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(r);
            ts[i].start();
        }

        log.info("Joining...");
        for (int i = 0; i < ts.length; i++) {
            ts[i].join();
        }

        System.out.println("R=" + c.i);
    }
}

class Counter {
    Lock lock;
    int i = 0;

    Counter(Lock lock) {
        this.lock = lock;
    }

    void inc() {
        lock.lock();
        lock.lock();

        i++;

        lock.unlock();
        lock.unlock();
    }
}

class StudentLock implements Lock {

    public static final long FREE = 0;

    final AtomicLong ownerId = new AtomicLong(FREE);

    @Override
    public void lock() {
        long currentId = Thread.currentThread().getId();
        while (ownerId.compareAndSet(FREE, currentId));
    }

    @Override
    public void unlock() {
        long currentId = Thread.currentThread().getId();
        boolean unlocked = ownerId.compareAndSet(currentId, FREE);
        if (!unlocked) {
            log.error("Not owner!");
        }
    }
}

class MyLock implements Lock {
    public static final long STUB = -1;
    AtomicLong owner = new AtomicLong(STUB);
    volatile int depth = 0;


    public void lock() {
        long threadId = Thread.currentThread().getId();
        boolean locked = false;
        if (depth >= 1) {
            locked = owner.compareAndSet(threadId, threadId);
        }
        if (!locked) {
            while (!owner.compareAndSet(STUB, threadId)) {};
        }
        depth++;
    }

    public void unlock() {
        long threadId = Thread.currentThread().getId();
        if (threadId == owner.get() && --depth == 0) {
            owner.set(STUB);
        }
    }
}


