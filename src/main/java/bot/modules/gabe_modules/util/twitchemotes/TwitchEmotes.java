package bot.modules.gabe_modules.util.twitchemotes;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;

/**
 * @since v0.3201
 */
public class TwitchEmotes extends ModuleBase {
    public enum EmoteSize { DEFAULT, MEDIUM, LARGE;}
    private String emotesInfo;

    private final String INFO = "emotes";
    private final String INFO_REGEX = Utils.TO_REGEX("emotes");
    private final String SIZE_1_REGEX = Utils.TO_REGEX("size1");
    private final String SIZE_2_REGEX = Utils.TO_REGEX("size2");
    private final String SIZE_3_REGEX = Utils.TO_REGEX("size3");

    final String Kappa = "Kappa";
    final String LUL = "LUL";
    final String OMEGALUL = "OMEGALUL";
    final String TheIlluminati = "TheIlluminati";
    final String FailFish = "FailFish";
    final String NotLikeThis = "NotLikeThis";
    final String ResidentSleeper = "ResidentSleeper";
    final String Kreygasm = "Kreygasm";
    final String DansGame = "DansGame";
    final String BrokeBack = "BrokeBack";
    final String Pepega = "Pepega";
    final String monkaS = "monkaS";
    final String POGGERS = "POGGERS";
    final String Pog = "Pog";
    final String haHAA = "haHAA";

    public TwitchEmotes(Chatbot chatbot) {
        super(chatbot);
        initInfo();
        Emote.setEmotesSize(Emote.SIZE_SMALL);
    }

    /**
     * @see EmoteSize#DEFAULT
     * @see EmoteSize#MEDIUM
     * @see EmoteSize#LARGE
     */
    public TwitchEmotes(Chatbot chatbot, int emoteSize) {
        super(chatbot);
        initInfo();
        Emote.setEmotesSize(emoteSize);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isOr(INFO_REGEX, INFO)) {
            chatbot.sendMessage(emotesInfo);
            return true;
        } else if (is(SIZE_1_REGEX) && message.getSender().getName().equals("Gabriel Wawerski")) {
            Emote.setEmotesSize(1);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        } else if (is(SIZE_2_REGEX) && message.getSender().getName().equals("Gabriel Wawerski")) {
            Emote.setEmotesSize(2);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        } else if (is(SIZE_3_REGEX) && message.getSender().getName().equals("Gabriel Wawerski")) {
            Emote.setEmotesSize(3);
            chatbot.sendMessage("Rozmiar zmieniony.");
            return true;
        } else if (is(Kappa)) { return sendEmote(Emote.Kappa.url()); }
        else if (is(LUL)) { return sendEmote(Emote.LUL.url()); }
        else if (is(OMEGALUL)) { return sendEmote(Emote.OMEGALUL.url()); }
        else if (is(TheIlluminati)) { return sendEmote(Emote.TheIlluminati.url()); }
        else if (is(FailFish)) { return sendEmote(Emote.FailFish.url()); }
        else if (is(NotLikeThis)) { return sendEmote(Emote.NotLikeThis.url()); }
        else if (is(ResidentSleeper)) { return sendEmote(Emote.ResidentSleeper.url()); }
        else if (is(Kreygasm)) { return sendEmote(Emote.Kreygasm.url()); }
        else if (is(DansGame)) { return sendEmote(Emote.DansGame.url()); }
        else if (is(BrokeBack)) { return sendEmote(Emote.BrokeBack.url()); }
        else if (is(Pepega)) { return sendEmote(Emote.Pepega.url()); }
        else if (is(monkaS)) { return sendEmote(Emote.monkaS.url()); }
        else if (is(POGGERS)) { return sendEmote(Emote.POGGERS.url()); }
        else if (is(Pog)) { return sendEmote(Emote.Pog.url()); }
        else if (is(haHAA)) { return sendEmote(Emote.haHAA.url()); }

        System.out.println("false");
        return false;
    }

    private boolean sendEmote(String emoteUrl) {
        System.out.println("URL: " + emoteUrl);
        chatbot.sendImageUrlWaitToLoad(emoteUrl);
        return true;
    }

    private void emotesInfo(String... emotes) {
        StringBuilder builder = new StringBuilder();
        for (String emote : emotes) {
            builder.append(emote).append("\n");
        }
        emotesInfo = builder.toString();
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, INFO_REGEX, INFO, SIZE_1_REGEX, SIZE_2_REGEX, SIZE_3_REGEX,
                Kappa, LUL, OMEGALUL, TheIlluminati, FailFish, NotLikeThis, ResidentSleeper, Kreygasm, DansGame,
                BrokeBack, Pepega, monkaS, POGGERS, Pog, haHAA);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(INFO_REGEX, INFO, SIZE_1_REGEX, SIZE_2_REGEX, SIZE_3_REGEX,
                Kappa, LUL, OMEGALUL, TheIlluminati, FailFish, NotLikeThis, ResidentSleeper, Kreygasm, DansGame,
                BrokeBack, Pepega, monkaS, POGGERS, Pog, haHAA);
    }

    private void initInfo() {
        emotesInfo(
                // twitchquotes
                Kappa,
                LUL,
                OMEGALUL,
                TheIlluminati,
                FailFish,
                NotLikeThis,
                ResidentSleeper,
                Kreygasm,
                DansGame,
                BrokeBack,

                // frankerfacez
                Pepega,
                monkaS,
                POGGERS,
                Pog,

                // bttv
                haHAA);
    }

    private void emotesInfo(Emote... emotes) {
        StringBuilder builder = new StringBuilder();
        for (Emote emote : emotes) {
            builder.append(emote.cmd()).append("\n");
        }
        emotesInfo = builder.toString();
    }
}
