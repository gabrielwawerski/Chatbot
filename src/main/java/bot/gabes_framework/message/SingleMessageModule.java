package bot.gabes_framework.message;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.simple.SimpleModule;

import java.util.List;

public abstract class SingleMessageModule extends SimpleModule {
    protected String message;

    // TODO make this the only constructor - other two break class's name agenda
    public SingleMessageModule(Chatbot chatbot, List<String> regexes, String message) {
        super(chatbot, regexes);
        this.message = message;
    }

    public SingleMessageModule(Chatbot chatbot, List<String> regexes) {
        super(chatbot, regexes);
    }

    public SingleMessageModule(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendMessage(this.message);
                return true;
            }
        }
        return false;
    }
}
