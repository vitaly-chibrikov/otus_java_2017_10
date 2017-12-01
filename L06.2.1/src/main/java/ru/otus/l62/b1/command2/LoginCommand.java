package ru.otus.l62.b1.command2;

/**
 *
 */
public class LoginCommand implements Command {
    // нет изменяемого состояния

    // есть только поля-компоненты
    AuthorizationService auth;

    public LoginCommand(AuthorizationService auth) {
        this.auth = auth;
    }

    @Override
    public void execute(Message msg) {
        LoginMessage message = (LoginMessage)msg;
        System.out.println(msg);
        auth.auth(message.login, message.pass);

    }
}
