package ru.otus.l62.b1.command2;

/**
 *
 */
public class TextCommand implements Command {
    ConversationService conversationService;

    public TextCommand(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Override
    public void execute(Message msg) {
        TextMessage message = (TextMessage) msg;
        System.out.println(msg);
        System.out.println("invoke conversation service()");
        conversationService.broadcast(message.msg);
    }
}
