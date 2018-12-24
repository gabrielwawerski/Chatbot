package bot.gabes_framework.util.simple;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;

import java.util.List;

public class SimpleUrlImageModule extends SimpleModule {
    private String url;
    private String message;

    public SimpleUrlImageModule(Chatbot chatbot, List<String> regexList, String url, String message) {
        super(chatbot, regexList);
        this.url = url;
        this.message = message;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendImageFromURLWithMessage(url, this.message);
                return true;
            }
        }
        return false;
    }
}
