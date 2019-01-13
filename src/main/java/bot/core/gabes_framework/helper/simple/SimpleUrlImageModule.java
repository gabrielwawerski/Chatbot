package bot.core.gabes_framework.helper.simple;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class SimpleUrlImageModule extends SimpleModule {
    private String url;
    private String message;

    public SimpleUrlImageModule(Chatbot chatbot, List<String> regexes, String url, String message) {
        super(chatbot, regexes);
        this.url = url;
        this.message = message;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isRegex()) {
            chatbot.sendImageFromURLWithMessage(url, this.message);
            return true;
        }
        return false;
    }
}
