package bot.modules.gabe_modules.image;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.gabes_framework.util.simple.SimpleModule;

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
                chatbot.sendImageUrlWaitToLoad("https://media.giphy.com/media/pUeXcg80cO8I8/giphy.gif"); // TODO get gif direct url
                return true;
            }
        }
        return false;
    }
}
