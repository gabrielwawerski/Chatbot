package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.List;

public class TwitchEmotes extends ModuleBase {
    private final List<String> regexes;

    private static final String URL = "https://cdn.frankerfacez.com/emoticon/128054/";

    private static final String _1_0 = "1.0";
    private static final String _2_0 = "2.0";
    private static final String _3_0 = "3.0";

    private static final String EMOTE_SIZE = _1_0;
    private static final String SIZE_1 = "1"; // for emotes with only one size available

    private final String EMOTES_REGEX = Utils.TO_REGEX("emotes");
    private final String EMOTES = "emotes";

    private final String OMEGALUL_REGEX = "OMEGALUL_REGEX";
    private final String OMEGALUL_URL = "https://cdn.frankerfacez.com/emoticon/128054/" + SIZE_1;


    private final String LUL_REGEX = "LUL_REGEX";
    private final String LUL_URL = "https://static-cdn.jtvnw.net/emoticons/v1/425618/" + EMOTE_SIZE;


    public TwitchEmotes(Chatbot chatbot) {
        super(chatbot);
        regexes = List.of(EMOTES_REGEX, EMOTES, OMEGALUL_REGEX, LUL_REGEX);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(LUL_REGEX)) {
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
        return findMatch(message, LUL_REGEX, OMEGALUL_REGEX);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(LUL_REGEX, OMEGALUL_REGEX);
    }
}
