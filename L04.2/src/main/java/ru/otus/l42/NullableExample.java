package ru.otus.l42;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class NullableExample {

    @NotNull
    static String parse(@Nullable Object object) {
        return null;
    }

    public static void main(String[] args) {
        parse(null);
    }

    @NotNull
    public List<String> getList(boolean cond) {
        if (cond) {
            return Arrays.asList("A", "B");
        } else {
            return Collections.emptyList();
        }
    }
}
