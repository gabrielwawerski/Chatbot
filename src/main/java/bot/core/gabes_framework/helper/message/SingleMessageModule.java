package bot.core.gabes_framework.helper.message;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.helper.simple.SimpleModule;

import java.util.List;

public abstract class SingleMessageModule extends SimpleModule {
    protected final String message;

    // TODO make this the only constructor - other two break class's name agenda
    public SingleMessageModule(Chatbot chatbot, List<String> regexes, String message) {
        super(chatbot, regexes);
        this.message = message;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexes) {
            if (match.equals(regex)) {
                chatbot.sendMessage(this.message);
                return true;
            }
        }
        return false;
    }
}
