package ru.otus.l71.structural.decorator;

/**
 * Created by tully.
 */
public class Main {
    public static void main(String[] args) {
        Printer normalPrinter = new PrinterImpl();
        normalPrinter.print("Test");

        Printer decorator = new PrinterDecorator(normalPrinter);
        decorator.print("Decorator");

        Printer reversedPrinter = new ReversedPrinter(normalPrinter);
        Printer upperCasePrinter = new UpperCasePrinter(reversedPrinter);

        normalPrinter.print("Optional wrapper in the Decorator pattern.");
        reversedPrinter.print("Optional wrapper in the Decorator pattern.");
        upperCasePrinter.print("Optional wrapper in the Decorator pattern.");
    }
}
