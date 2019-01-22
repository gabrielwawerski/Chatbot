package bot.modules.gabe.util.twitch_emotes;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.database.Users;
import bot.modules.gabe.point_system.util.Points;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.gabes_framework.core.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static bot.core.gabes_framework.core.util.Utils.msgBy;
import static bot.modules.gabe.util.twitch_emotes.Emote.*;

/**
 * @since v0.3201
 */
public class TwitchEmotes extends ModuleBase {
    public enum EmoteSize { DEFAULT, MEDIUM, LARGE}
    private String emotesInfo;
    private static List<Emote> EMOTES;

    private static final String INFO_SIMPLE = "emotes";
    private static final String INFO_REGEX = Utils.TO_REGEX("emotes");
    private static final String SIZE_1_REGEX = Utils.TO_REGEX("size1");
    private static final String SIZE_2_REGEX = Utils.TO_REGEX("size2");
    private static final String SIZE_3_REGEX = Utils.TO_REGEX("size3");


    public TwitchEmotes(Chatbot chatbot) {
        super(chatbot);
        EMOTES = Emote.getEmotes();
        setEmotesSize(SIZE_LARGE);
        initInfo();
    }

    /**
     * @see EmoteSize#DEFAULT
     * @see EmoteSize#MEDIUM
     * @see EmoteSize#LARGE
     */
    public TwitchEmotes(Chatbot chatbot, int emoteSize) {
        super(chatbot);
        initInfo();
        setEmotesSize(emoteSize);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOr(INFO_REGEX, INFO_SIMPLE)) {
            chatbot.sendMessage(emotesInfo);
            return true;
        } else if (is(SIZE_1_REGEX) && msgBy(message, Users.Gabe)) {
            setEmotesSize(1);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        } else if (is(SIZE_2_REGEX) && msgBy(message, Users.Gabe)) {
            setEmotesSize(2);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        } else if (is(SIZE_3_REGEX) && msgBy(message, Users.Gabe)) {
            setEmotesSize(3);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        }

        for (Emote current : EMOTES) {
            if (match.equals(current.value())) {
                pushPoints(message, Points.POINTS_TWITCHEMOTES_REGEX);
                return sendEmoteMsg(current);
            }
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        EMOTES = Emote.getEmotes();
        ArrayList<String> returnList = new ArrayList<>(EMOTES.size());

        returnList.add(INFO_REGEX);
        returnList.add(INFO_SIMPLE);
        returnList.add(SIZE_1_REGEX);
        returnList.add(SIZE_2_REGEX);
        returnList.add(SIZE_3_REGEX);

        for (Emote emote : EMOTES) {
            returnList.add(emote.value());
        }

        return returnList;
    }

    private boolean isEmote(Emote emote) {
        return match.equals(EMOTES.get(EMOTES.indexOf(emote)).value());
    }

    private boolean sendEmoteMsg(Emote emote) {
        chatbot.sendImageUrlWaitToLoad(emote.url());
        return true;
    }

    private void initInfo() {
        StringBuilder builder = new StringBuilder();
        for (Emote emote : EMOTES) {
            builder.append(emote.value()).append("\n");
        }
        emotesInfo = builder.toString();
    }
}
