package bot.modules.gabe.twitchemotes;

import java.util.List;

import static bot.modules.gabe.twitchemotes.Emote.Src.*;

public class Emote {
    private String command;
    private String url;
    private Src src;

    public enum Src {
        TQUOTES, FFZ, BTTV,
        TQUOTES_DEFAULT, FFZ_DEFAULT, BTTV_DEFAULT,
        TQUOTES_MEDIUM, FFZ_MEDIUM, BTTV_MEDIUM,
        TQUOTES_LARGE, FFZ_LARGE, BTTV_LARGE
    }

    public enum Size {
        DEFAULT, MEDIUM, LARGE
    }

    public static final int SIZE_DEFAULT = 1;
    public static final int SIZE_MEDIUM = 2;
    public static final int SIZE_LARGE = 3;

    //region emote sources size dependent vars
    private static String TWITCHQUOTES_SIZE;
    private static String FRANKERFACEZ_SIZE;
    private static String BTTV_SIZE;

    private static final String TQUOTES_DEFAULT_FIX = "1.0";
    private static final String TQUOTES_MEDIUM_FIX = "2.0";
    private static final String TQUOTES_LARGE_FIX = "3.0";

    private static final String FFZ_DEFAULT_FIX = "1";
    private static final String FFZ_MEDIUM_FIX = "2";
    private static final String FFZ_LARGE_FIX = "4";

    private static final String BTTV_DEFAULT_FIX = "1x";
    private static final String BTTV_MEDIUM_FIX = "2x";
    private static final String BTTV_LARGE_FIX = "3x";


    private static final String TQUOTES_SIZE_DEFAULT = "1"; // for emotes with only one size available
    private static final String FFZ_SIZE_DEFAULT_FIX = "1"; // for emotes with only one size available
    //endregion

    //region emote definitions
    public static final Emote Kappa = new Emote("Kappa",
            "https://static-cdn.jtvnw.net/emoticons/v1/25/",
            TQUOTES);
    public static final Emote LUL = new Emote("LUL",
            "https://static-cdn.jtvnw.net/emoticons/v1/425618/",
            TQUOTES);
    public static final Emote OMEGALUL = new Emote("OMEGALUL",
            "https://cdn.frankerfacez.com/emoticon/128054/",
            FFZ_DEFAULT);
    public static final Emote TheIlluminati = new Emote("TheIlluminati",
            "https://static-cdn.jtvnw.net/emoticons/v1/145315/",
            TQUOTES);
    public static final Emote FailFish = new Emote("FailFish",
            "https://static-cdn.jtvnw.net/emoticons/v1/360/",
            TQUOTES);
    public static final Emote NotLikeThis = new Emote("NotLikeThis",
            "https://static-cdn.jtvnw.net/emoticons/v1/58765/",
            TQUOTES);
    public static final Emote ResidentSleeper = new Emote("ResidentSleeper",
            "https://static-cdn.jtvnw.net/emoticons/v1/245/",
            TQUOTES);
    public static final Emote Kreygasm = new Emote("Kreygasm",
            "https://static-cdn.jtvnw.net/emoticons/v1/41/",
            TQUOTES);
    public static final Emote DansGame = new Emote("DansGame",
            "https://static-cdn.jtvnw.net/emoticons/v1/33/",
            TQUOTES);
    public static final Emote BrokeBack = new Emote("BrokeBack",
            "https://static-cdn.jtvnw.net/emoticons/v1/4057/",
            TQUOTES);
    public static final Emote Pepega = new Emote("Pepega",
            "https://cdn.frankerfacez.com/emoticon/243789/",
            FFZ);
    public static final Emote monkaS = new Emote("monkaS",
            "https://cdn.frankerfacez.com/emoticon/130762/",
            FFZ);
    public static final Emote POGGERS = new Emote("POGGERS",
            "https://cdn.frankerfacez.com/emoticon/214129/",
            FFZ_DEFAULT);
    public static final Emote Pog = new Emote("Pog",
            "https://cdn.frankerfacez.com/emoticon/210748/",
            FFZ_MEDIUM);
    public static final Emote haHAA = new Emote("haHAA",
            "https://cdn.betterttv.net/emote/555981336ba1901877765555/",
            BTTV);
    //endregion

    public static  List<Emote> emotes() {
        return List.of(
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
                Pepega,
                monkaS,
                POGGERS,
                Pog,
                haHAA);
    }

    public String cmd() {
        return command;
    }

    public String url() {
        if (src == TQUOTES) {
            return url + TWITCHQUOTES_SIZE;
        } else if (src == TQUOTES_DEFAULT) {
            return url + TQUOTES_SIZE_DEFAULT;
        } else if (src == TQUOTES_MEDIUM) {
            return url + TQUOTES_MEDIUM_FIX;
        } else if (src == TQUOTES_LARGE) {
            return url + TQUOTES_LARGE_FIX;
        }

        else if (src == FFZ) {
            return url + FRANKERFACEZ_SIZE;
        } else if (src == FFZ_DEFAULT) {
            return url + FFZ_DEFAULT_FIX;
        } else if (src == FFZ_MEDIUM) {
            return url + FFZ_MEDIUM_FIX;
        } else if (src == FFZ_LARGE) {
            return url + FFZ_LARGE_FIX;
        }

        else if (src == BTTV) {
            return url + BTTV_SIZE;
        } else if (src == BTTV_DEFAULT) {
            return url + BTTV_DEFAULT_FIX;
        } else if (src == BTTV_MEDIUM) {
            return url + FFZ_MEDIUM_FIX;
        } else if (src == BTTV_LARGE) {
            return url + BTTV_LARGE_FIX;
        }
        return "Błąd";
    }

    @SuppressWarnings("Duplicates")
    String urlWanted(Size size) {
        if (size == Size.DEFAULT) {
            if (src == TQUOTES || src == TQUOTES_DEFAULT) {
                return url + TQUOTES_DEFAULT_FIX;
            } else if (src == FFZ || src == FFZ_DEFAULT) {
                return url + FFZ_DEFAULT_FIX;
            } else if (src == BTTV || src == BTTV_DEFAULT) {
                return url + BTTV_DEFAULT_FIX;
            }

        } else if (size == Size.MEDIUM) {
            if (src == TQUOTES || src == TQUOTES_MEDIUM) {
                return url + TQUOTES_MEDIUM_FIX;
            } else if (src == TQUOTES_DEFAULT) {
                return url + TQUOTES_DEFAULT_FIX;
            }

            if (src == FFZ || src == FFZ_MEDIUM) {
                return url + FFZ_MEDIUM_FIX;
            } else if (src == FFZ_DEFAULT) {
                return url + FFZ_DEFAULT_FIX;
            }

            if (src == BTTV || src == BTTV_MEDIUM) {
                return url + BTTV_MEDIUM_FIX;
            } else if (src == BTTV_DEFAULT) {
                return url + BTTV_DEFAULT_FIX;
            }

        } else if (size == Size.LARGE) {
            if (src == TQUOTES || src == TQUOTES_LARGE) {
                return url + TQUOTES_LARGE_FIX;
            } else if (src == TQUOTES_MEDIUM) {
                return url + TQUOTES_MEDIUM_FIX;
            } else if (src == TQUOTES_DEFAULT) {
                return url + TQUOTES_DEFAULT_FIX;
            }

            if (src == FFZ || src == FFZ_LARGE) {
                return url + FFZ_LARGE_FIX;
            } else if (src == FFZ_MEDIUM) {
                return url + FFZ_MEDIUM_FIX;
            } else if (src == FFZ_DEFAULT) {
                return url + FFZ_DEFAULT_FIX;
            }

            if (src == BTTV || src == BTTV_LARGE) {
                return url + BTTV_LARGE_FIX;
            } else if (src == BTTV_MEDIUM) {
                return url + BTTV_MEDIUM_FIX;
            } else if (src == BTTV_DEFAULT) {
                return url + BTTV_DEFAULT_FIX;
            }
        }
        System.out.println("falling back to url()");
        return url();
    }

    public static void setEmotesSize(int SIZE) {
        if (SIZE == SIZE_DEFAULT) {
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
}
