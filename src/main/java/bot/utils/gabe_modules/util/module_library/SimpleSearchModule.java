package bot.utils.gabe_modules.util.module_library;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;

public class SimpleSearchModule extends SimpleModule {
    public SimpleSearchModule(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }
}
