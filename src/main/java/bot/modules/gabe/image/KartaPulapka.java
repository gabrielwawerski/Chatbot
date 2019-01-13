package bot.modules.gabe.image;

import bot.core.Chatbot;
import bot.core.PcionBot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class KartaPulapka extends ModuleBase {
    public KartaPulapka(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : regexes) {
            if (match.equals(command)) {
                addPoints(message, Utils.POINTS_KARTAPULAPKA_REGEX);
                chatbot.sendImageUrlWaitToLoad(PcionBot.KARTAPULAPKA_IMG_URL);
                return true;
            }
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return getMappedRegexes("karta", "kartapulapka", "myk");
    }
}
