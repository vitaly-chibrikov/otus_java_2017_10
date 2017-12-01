package ru.otus.l62.b1.command;

/**
 *
 */
public class LoginCommand implements Command {

    String login, pass;

    public LoginCommand(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public void execute() {
        System.out.println("Logged in with " + login + ", " + pass);
    }
}
