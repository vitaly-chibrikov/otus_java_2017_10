package ru.otus.l62.b1.command;

/**
 *
 */
public class CommandReceiver {

    public void accept(Command command) {
        command.execute();
    }
}
