package ru.otus.l62.b1.command;

import java.util.Scanner;

import ru.otus.l62.b1.command2.MessageReceiver;

/**
 *
 */
public class ConsoleInput {

    public static void main(String[] args) {

        CommandReceiver receiver = new CommandReceiver();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.startsWith("/")) {
                String[] tokens = line.split(" ");
                String cmd = tokens[0].substring(1);
                switch (cmd) {
                    case "login":
                        receiver.accept(new LoginCommand(tokens[1], tokens[2]));
                        break;
                    case "text":
                        receiver.accept(new TextCommand(tokens[1]));
                        break;
                    default:

                }
            }
        }
    }
}
