package bot.modules.gabe_modules.util.twitchemotes;

import java.util.List;

public class Emote {
    private String command;
    private String url;
    private Src src;

    private static String TWITCHQUOTES_SIZE;
    private static String FRANKERFACEZ_SIZE;
    private static String BTTV_SIZE;

    private static final String TWITCHQUOTES_DEFAULT = "1.0";
    private static final String TWITCHQUOTES_MEDIUM = "2.0";
    private static final String TQUOTES_LARGE = "3.0";

    private static final String FRANKERFACEZ_DEFAULT = "1";
    private static final String FFZZ_MEDIUM = "2";
    private static final String FFZ_LARGE = "4";

    private static final String BTTV_DEFAULT = "1x";
    private static final String BTTV_MEDIUM = "2x";
    private static final String BTTV_LARGE = "3x";

    public enum Src {
        TQUOTES, FFZ, BTTV,
        TQUOTES_DEFAULT, FFZ_DEFAULT, SINGLE_BTTV
    }

    public static final int SIZE_SMALL = 1;
    public static final int SIZE_MEDIUM = 2;
    public static final int SIZE_LARGE = 3;

    private static final String TQUOTES_SIZE_DEFAULT = "1"; // for emotes with only one size available
    private static final String FFZ_SIZE_DEFAULT = "1"; // for emotes with only one size available

    public static final Emote Kappa = new Emote("Kappa",
            "https://static-cdn.jtvnw.net/emoticons/v1/25/",
            Src.TQUOTES);
    public static final Emote LUL = new Emote("LUL",
            "https://static-cdn.jtvnw.net/emoticons/v1/425618/",
            Src.TQUOTES);
    public static final Emote OMEGALUL = new Emote("OMEGALUL",
            "https://cdn.frankerfacez.com/emoticon/128054/",
            Src.FFZ_DEFAULT);
    public static final Emote TheIlluminati = new Emote("TheIlluminati",
            "https://static-cdn.jtvnw.net/emoticons/v1/145315/",
            Src.TQUOTES);
    public static final Emote FailFish = new Emote("FailFish",
            "https://static-cdn.jtvnw.net/emoticons/v1/360/",
            Src.TQUOTES);
    public static final Emote NotLikeThis = new Emote("NotLikeThis",
            "https://static-cdn.jtvnw.net/emoticons/v1/58765/",
            Src.TQUOTES);
    public static final Emote ResidentSleeper = new Emote("ResidentSleeper",
            "https://static-cdn.jtvnw.net/emoticons/v1/245/",
            Src.TQUOTES);
    public static final Emote Kreygasm = new Emote("Kreygasm",
            "https://static-cdn.jtvnw.net/emoticons/v1/41/",
            Src.TQUOTES);
    public static final Emote DansGame = new Emote("DansGame",
            "https://static-cdn.jtvnw.net/emoticons/v1/33/",
            Src.TQUOTES);
    public static final Emote BrokeBack = new Emote("BrokeBack",
            "https://static-cdn.jtvnw.net/emoticons/v1/4057/",
            Src.TQUOTES);
    public static final Emote Pepega = new Emote("Pepega",
            "https://cdn.frankerfacez.com/emoticon/243789/",
            Src.FFZ);
    public static final Emote monkaS = new Emote("monkaS",
            "https://cdn.frankerfacez.com/emoticon/130762/",
            Src.FFZ);
    public static final Emote POGGERS = new Emote("POGGERS",
            "https://cdn.frankerfacez.com/emoticon/214129/",
            Src.FFZ_DEFAULT);
    public static final Emote Pog = new Emote("Pog",
            "https://cdn.frankerfacez.com/emoticon/210748/",
            Src.FFZ);
    public static final Emote haHAA = new Emote("haHAA",
            "https://cdn.betterttv.net/emote/555981336ba1901877765555/",
            Src.BTTV);

    public static  List<String> getCommands() {
        return List.of(
                Kappa.command,
                LUL.command,
                OMEGALUL.command,
                TheIlluminati.command,
                FailFish.command,
                NotLikeThis.command,
                ResidentSleeper.command,
                Kreygasm.command,
                DansGame.command,
                BrokeBack.command,
                Pepega.command,
                monkaS.command,
                POGGERS.command,
                Pog.command,
                haHAA.command);
    }

    public String cmd() {
        return command;
    }

    private Emote setCmd(String command) {
        this.command = command;
        return this;
    }

    private Emote setUrl(String url) {
        this.url = url;
        return this;
    }

    private Emote(String command, String url, Src src) {
        this.command = command;
        this.url = url;
        this.src = src;
    }

    private Emote(Src src) {
        this.src = src;
    }

    public String url() {
        if (command.equals(Pog.command) && TWITCHQUOTES_SIZE.equals(TQUOTES_LARGE)) {
            return url + FFZZ_MEDIUM;
        }

        if (src == Src.TQUOTES) {
            return url + TWITCHQUOTES_SIZE;
        } else if (src == Src.TQUOTES_DEFAULT) {
            return url + TQUOTES_SIZE_DEFAULT;
        } else if (src == Src.FFZ) {
            return url + FRANKERFACEZ_SIZE;
        } else if (src == Src.FFZ_DEFAULT) {
            return url + FFZ_SIZE_DEFAULT;
        } else if (src == Src.BTTV) {
            return url + BTTV_SIZE;
        }
        return "Błąd";
    }

    public static void setEmotesSize(int SIZE) {
        if (SIZE == SIZE_SMALL) {
            TWITCHQUOTES_SIZE = "1.0";
            FRANKERFACEZ_SIZE = "1";
            BTTV_SIZE = "1x";
        } else if (SIZE == SIZE_MEDIUM) {
            TWITCHQUOTES_SIZE = "2.0";
            FRANKERFACEZ_SIZE = "2";
            BTTV_SIZE = "2x";
        } else if (SIZE == SIZE_LARGE) {
            TWITCHQUOTES_SIZE = "3.0";
            FRANKERFACEZ_SIZE = "4";
            BTTV_SIZE = "3x";
        }
    }
}
