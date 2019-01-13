package bot.core.gabes_framework.framework.message;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

public abstract class SingleMessageModule extends ModuleBase {
    protected final String message;

    public SingleMessageModule(Chatbot chatbot, String message) {
        super(chatbot);
        this.message = message;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isRegex()) {
            chatbot.sendMessage(this.message);
            return true;
        }
        return false;
    }
}
