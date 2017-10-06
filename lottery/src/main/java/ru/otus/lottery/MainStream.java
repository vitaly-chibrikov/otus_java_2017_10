package ru.otus.lottery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

/*
https://github.com/vitaly-chibrikov/otus_mai_java_2017_10
*/

public class MainStream {
    public static void main(String[] args) throws IOException {
        String salt = "Звягин Дмитрий\u200Bview -> Enter presentation mode\uFEFF\n" +
                "\n" +
                "Vadim Tishenko\u200B\uD83D\uDE09\uFEFF\n" +
                "\n" +
                "Konstantin Balin\u200Bшанс что в hashmap в один бакет попадут все строки\uFEFF минимален)\n" +
                "\n" +
                "Татьяна Ануфриева\u200BА запуск вашего кода по строкам с выводом рез-та ответом типа консоли возможен?\uFEFF\n" +
                "\n" +
                "Владимир Капинос\u200Bsha3\uFEFF";

        Files.lines(Paths.get("sql-54.csv"))
                .map(line -> line.replace("\"", ""))
                .map(line -> line.substring(0, line.indexOf("@")))
                .map(line -> line + "\t" + salt)
                .sorted(Comparator.comparingLong(String::hashCode))
                .map(line -> line.hashCode() + "\t" + line.replace(salt,""))
                .forEach(System.out::println);
    }
}
