package ru.otus.l62.b1.command2;

/**
 *
 */
public class LoginMessage extends Message {

    public String login, pass;

    public LoginMessage(String login, String pass) {
        super(Type.LOGIN);
        this.login = login;
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
