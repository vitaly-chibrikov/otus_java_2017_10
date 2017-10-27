package bytecode;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class Bean {
    Integer val = 0;
    short shortVal = 1;
    Byte byteField;
    String str = "dead";
    int[] array = new int[10];
    int[] arrayEmpty = new int[0];
    

    public Bean() {
        Arrays.fill(array, 1);
    }

    public static void main(String[] args) {
        Bean bean = new Bean();

        while (true) {
            bean.val += 1;
            int[] arr = new int[10];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = bean.val;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
