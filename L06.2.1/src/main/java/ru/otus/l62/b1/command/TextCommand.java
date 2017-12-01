package ru.otus.l62.b1.command;

/**
 *
 */
public class TextCommand implements Command {

    String text;

    public TextCommand(String text) {
        this.text = text;
    }

    @Override
    public void execute() {
        System.out.println(text);
    }
}
