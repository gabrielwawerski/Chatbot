package bot.utils.gabe_modules.util.module_library;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;

import java.util.List;

public abstract class SingleMessageModule extends SimpleModule {
    protected String message;

    public SingleMessageModule(Chatbot chatbot, List<String> commands, String message) {
        super(chatbot, commands);
        this.message = message;
    }

    public SingleMessageModule(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    public SingleMessageModule(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : commands) {
            if (match.equals(command)) {
                chatbot.sendMessage(this.message);
                return true;
            }
        }
        return false;
    }
}
