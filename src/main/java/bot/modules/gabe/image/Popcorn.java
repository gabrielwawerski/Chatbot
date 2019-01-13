package bot.modules.gabe.image;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class Popcorn extends ModuleBase {
    public Popcorn(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) {
        updateMatch(message);

        if (isRegex()) {
            addPoints(message, Utils.POINTS_POPCORN_REGEX);
            chatbot.sendImageUrlWaitToLoad("https://media.giphy.com/media/pUeXcg80cO8I8/giphy.gif"); // TODO get gif direct url
            return true;
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return getMappedRegexes("popcorn", "rajza");
    }
}
