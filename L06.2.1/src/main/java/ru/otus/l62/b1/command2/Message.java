package ru.otus.l62.b1.command2;

/**
 *
 */
public abstract class Message {
    enum Type {
        LOGIN, TEXT
    }

    Type type;

    public Message(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
