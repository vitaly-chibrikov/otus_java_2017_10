package beans;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * $java -XX:-UseCompressedOops -javaagent:L2-agent-agent.jar -cp L2-agent-main.jar beans.Sleeping
 */
public class Sleeping {
    public void sleepNow() throws Exception {
        Random r = new Random();
        int sleepTime = r.nextInt(2000);
        System.out.println(sleepTime);

        TimeUnit.MILLISECONDS.sleep(r.nextInt(2000));
    }

    public static void main(String[] args) throws Exception {
        new Sleeping().sleepNow();
    }
}
