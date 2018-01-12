package ru.otus.l42;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.gson.Gson;


/**
 *
 */
@RunWith(Parameterized.class)
public class JsonSerializerTest {

    static class Sample {
        @Nullable
        private Object obj;

        public Sample(@Nullable Object obj) {
            this.obj = obj;
        }

        @Nullable
        public Object getObj() {
            return obj;
        }
    }

    @NotNull
    private final Sample sample;

    private static Gson gson;

    @BeforeClass
    public static void init() {
        System.out.println("beforeClass");
        gson = new Gson();
    }


    public JsonSerializerTest(@NotNull Sample sample) {
        this.sample = sample;
    }

    @Parameterized.Parameters
    public static Collection<Sample> data() {

        Map<Long, String> map = new HashMap<>();
        map.put(1L, "Abc");
        map.put(2L, "Abc");
        map.put(Long.MAX_VALUE, "X");

        Bean bean = new Bean();
        bean.i1 = 1;
        bean.l1 = Long.MAX_VALUE;
        bean.s1 = "A";

        return Arrays.asList(
                new Sample(null),
                new Sample('w'),
                new Sample("Hello"),
                new Sample(true),
                new Sample(123),
                new Sample(1.23),
                new Sample(new int[]{1, 2, 3}),
                new Sample(new String[]{"A", "B", "C"}),
                new Sample(Arrays.asList("A", "B", "C")),
                new Sample(map),
                new Sample(bean),
                new Sample(Complex.getInstance())
        );
    }

    @Test
    public void toJson() throws Exception {
        final String expected = gson.toJson(sample.getObj());
        System.out.println(expected);
    }

    static class Complex {
        int a;
        String s;
        Map<String, List<Integer>> map;

        public Complex(int a, String s, Map<String, List<Integer>> map) {
            this.a = a;
            this.s = s;
            this.map = map;
        }

        static Complex getInstance() {
            Map<String, List<Integer>> map = new HashMap<>();
            map.put("A", Arrays.asList(1, 2, 3));
            map.put("B", Arrays.asList(1, 2, 3, 4));
            return new Complex(1, "A", map);
        }
    }
}
