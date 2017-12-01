package ru.otus.l62.b1.command2;

/**
 *
 */
public class AuthorizationService {
    public boolean auth(String login, String pass) {
        System.out.println("invoke auth service()");
        return (login.equals("admin") && pass.equals("123"));
    }
}
