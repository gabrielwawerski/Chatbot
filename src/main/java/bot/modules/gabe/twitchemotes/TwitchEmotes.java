package bot.modules.gabe.twitchemotes;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.point_system.UserEntry;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.ModuleBase;
import bot.core.gabes_framework.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static bot.core.gabes_framework.util.Utils.msgIsBy;
import static bot.modules.gabe.twitchemotes.Emote.*;

/**
 * @since v0.3201
 */
public class TwitchEmotes extends ModuleBase {
    public enum EmoteSize { DEFAULT, MEDIUM, LARGE;}
    private String emotesInfo;
    private List<Emote> EMOTES;

    private final String INFO = "emotes";
    private final String INFO_REGEX = Utils.TO_REGEX("emotes");
    private final String SIZE_1_REGEX = Utils.TO_REGEX("size1");
    private final String SIZE_2_REGEX = Utils.TO_REGEX("size2");
    private final String SIZE_3_REGEX = Utils.TO_REGEX("size3");


    public TwitchEmotes(Chatbot chatbot) {
        super(chatbot);
        EMOTES = Emote.emotes();
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

        if (isOr(INFO_REGEX, INFO)) {
            chatbot.sendMessage(emotesInfo);
            return true;
        } else if (is(SIZE_1_REGEX) && msgIsBy(message, UserEntry.Gabe)) {
            setEmotesSize(1);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        } else if (is(SIZE_2_REGEX) && msgIsBy(message, UserEntry.Gabe)) {
            setEmotesSize(2);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        } else if (is(SIZE_3_REGEX) && msgIsBy(message, UserEntry.Gabe)) {
            setEmotesSize(3);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        }
        for (Emote current : EMOTES) {
            if (match.equals(current.cmd())) {
                return sendEmoteMsg(current);
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, getCmds());
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(getCmds());
    }

    private boolean isEmote(Emote emote) {
        return match.equals(EMOTES.get(EMOTES.indexOf(emote)).cmd());
    }

    private boolean sendEmoteMsg(Emote emote) {
        chatbot.sendImageUrlWaitToLoad(emote.url());
        return true;
    }

    private ArrayList<String> getCmds() {
        ArrayList<String> regexes = new ArrayList<>(EMOTES.size());
        regexes.add(INFO_REGEX);
        regexes.add(INFO);
        regexes.add(SIZE_1_REGEX);
        regexes.add(SIZE_2_REGEX);
        regexes.add(SIZE_3_REGEX);

        for (Emote emote : EMOTES) {
            regexes.add(emote.cmd());
        }
        return regexes;
    }

    private void initInfo() {
        StringBuilder builder = new StringBuilder();
        for (Emote emote : EMOTES) {
            builder.append(emote.cmd()).append("\n");
        }
        emotesInfo = builder.toString();
    }
}
