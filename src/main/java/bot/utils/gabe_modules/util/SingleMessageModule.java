package bot.utils.gabe_modules.util;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;

import java.util.List;

public class SingleMessageModule extends ModuleBase {
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
