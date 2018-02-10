package ru.otus.l141;

class NonBlockingLogger {

    @SuppressWarnings("InfiniteLoopStatement")
    void start() throws InterruptedException {
        NonBlockingLogger logger = new NonBlockingLogger();
        while (true) {
            Thread.sleep(500);
            logger.log(String.valueOf(System.nanoTime()));
        }
    }

    private void log(String message) {
        new Thread(() -> {
            System.out.println(System.nanoTime() + " " + message);
        }).start();
    }
}
