package ru.otus.l62.b1.command2;

import java.util.Scanner;

/**
 *
 */
public class ConsoleInput {

    public static void main(String[] args) {

        MessageReceiver receiver = new MessageReceiver();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.startsWith("/")) {
                String[] tokens = line.split(" ");
                String cmd = tokens[0].substring(1);
                switch (cmd) {
                    case "login":
                        receiver.accept(new LoginMessage(tokens[1], tokens[2]));
                        break;
                    case "text":
                        receiver.accept(new TextMessage(tokens[1]));
                        break;
                    default:

                }
            }
        }
    }
}
