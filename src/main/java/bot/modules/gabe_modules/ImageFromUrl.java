package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.simple.SimpleUrlImageModule;

import java.util.List;

public class ImageFromUrl extends SimpleUrlImageModule {
    private static final String TEST_REGEX = Utils.actionify("test");

    public ImageFromUrl(Chatbot chatbot, List<String> regexList, String url, String message) {
        super(chatbot, regexList, url, message);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String regex : regexList) {
            if (match.equals(regex)) {
                chatbot.sendMessageWaitToLoad(new Message("https://i.imgur.com/s6q5qCG.jpg", null));
                return true;
            }
        }
        return false;
    }
}
