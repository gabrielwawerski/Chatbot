package bot.modules.gabe.image;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.Utils;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.simple.SimpleModule;

import java.util.List;

public class Popcorn extends SimpleModule {
    public Popcorn(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    @Override
    public boolean process(Message message) {
        updateMatch(message);

        for (String command : regexList) {
            if (match.equals(command)) {
                addPoints(message, Utils.POINTS_POPCORN_REGEX);
                chatbot.sendImageUrlWaitToLoad("https://media.giphy.com/media/pUeXcg80cO8I8/giphy.gif"); // TODO get gif direct url
                return true;
            }
        }
        return false;
    }
}
