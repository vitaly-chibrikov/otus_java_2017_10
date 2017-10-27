package beans;

import java.util.HashMap;

import org.jetbrains.annotations.NotNull;
import org.openjdk.jol.info.GraphLayout;

import agent.SimpleAgent;
import jol.ObjectLayer;

/**
 * GRADLE
 * $gradle clean mainJar agentJar
 * $cd build/libs/
 * $java -XX:-UseCompressedOops -javaagent:L2-agent-agent.jar -cp L2-agent-main.jar beans.Sample
 * try -XX:+UseCompressedOops
 *
 *
 * MVN
 * $mvn clean package
 * $cd target
 * $java -XX:-UseCompressedOops -javaagent:L2-agent-1.0-SNAPSHOT-jar-with-dependencies.jar -cp L2-agent-1.0-SNAPSHOT-jar-with-dependencies.jar beans.Sample
 *
 */
public class Sample {
    public static void printSize(final @NotNull Object object) {
        System.out.println(String.format("%-30s(%-25s)\t %-10s\t%-10s\t%-10s\n",
                object.toString(),
                object.getClass(),
                SimpleAgent.getObjectSize(object),
                ObjectLayer.layout(object).instanceSize(),
                GraphLayout.parseInstance(object).totalSize()
        ));
    }

    public static void main(final String[] arguments) {
        SimpleAgent.printInfo();

        /*
          TODO: Надо ли мерить размеры примитивов?
           */

        System.out.println(String.format("%-30s %-25s\t %-10s\t%-10s\t%-10s\n", "toString", "class", "Agent", "objLayer", "graph"));

        printSize(new Object());
        printSize(new String());
        printSize(new String("a"));

        printSize(new int[] {1});
        printSize(new int[] {1, 2, 3});

        printSize(new Long(1));
        printSize(new Boolean(false));

        printSize(new HashMap<>());

        printSize(new Bean());


        // TODO: check summary size of String+Bean
        Bean bean = new Bean();
        bean.s1 = "hello";
        printSize(bean);
        printSize(bean.s1);

        // TODO: padding with boolean fields
        printSize(new Bean2());
    }

}
