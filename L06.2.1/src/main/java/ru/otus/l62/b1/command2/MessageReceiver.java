package ru.otus.l62.b1.command2;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MessageReceiver {
    static Map<Message.Type, Command> cmd = new HashMap<>();
    static {
        cmd.put(Message.Type.TEXT, new TextCommand(new ConversationService()));
        cmd.put(Message.Type.LOGIN, new LoginCommand(new AuthorizationService()));
    }

    public void accept(Message msg) {
        cmd.get(msg.getType()).execute(msg);
    }
}
