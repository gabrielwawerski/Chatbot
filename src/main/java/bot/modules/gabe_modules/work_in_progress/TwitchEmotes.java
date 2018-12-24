package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.List;

public class TwitchEmotes extends ModuleBase {
    private static final String URL = "https://cdn.frankerfacez.com/emoticon/128054/";

    private static final String EMOTE_1 = "1.0";
    private static final String EMOTE_2 = "2.0";
    private static final String EMOTE_3 = "3.0";
    private static final String EMOTE_SIZE_SINGLE = "1"; // for emotes with only one size available
    private static final String EMOTE_SIZE = EMOTE_1;

    private final String INFO_REGEX = Utils.TO_REGEX("emotes");
    private final String INFO = "emotes";

    private final String OMEGALUL_REGEX = "OMEGALUL";
    private final String OMEGALUL_URL = "https://cdn.frankerfacez.com/emoticon/128054/" + EMOTE_SIZE_SINGLE;

    private final String LUL_REGEX = "LUL";
    private final String LUL_URL = "https://static-cdn.jtvnw.net/emoticons/v1/425618/" + EMOTE_SIZE;

    public TwitchEmotes(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOr(INFO_REGEX, INFO)) {
            chatbot.sendMessage("LUL\nOMEGALUL");
            return true;
        } else if (is(LUL_REGEX)) {
            chatbot.sendImageUrlWaitToLoad(LUL_URL);
            return true;
        } else if (is(OMEGALUL_REGEX)) {
            chatbot.sendImageUrlWaitToLoad(OMEGALUL_URL);
            return true;
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, INFO_REGEX, INFO, OMEGALUL_REGEX, LUL_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(INFO_REGEX, INFO, OMEGALUL_REGEX, LUL_REGEX);
    }
}
