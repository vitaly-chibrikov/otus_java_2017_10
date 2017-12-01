package ru.otus.l62.b1.command2;

/**
 *
 */
public class TextMessage extends Message {
    public String msg;

    public TextMessage(String msg) {
        super(Type.TEXT);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
