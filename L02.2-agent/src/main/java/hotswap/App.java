package hotswap;

import java.util.concurrent.TimeUnit;

/**
 * -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9999
 *
 *
 * suspend=y/n
 */
public class App {

    long counter = 0;

    public App() {
        System.out.println("in construct");
    }

    public static void main(String[] args) {
        new App().doWork();
    }

    public void doWork() {
        while (true) {
            counter += 1;
//            inc();
            System.out.println("counter hello: " + counter);

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void inc() {
        counter += 100;
    }
}
