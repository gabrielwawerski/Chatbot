package bot.gabes_framework.core.libs;

import bot.core.helper.interfaces.Util;
import bot.core.helper.misc.Message;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * I have only added javadocs. All credit goes to hollandjake.
 *
 * @since 0.30
 * @version 1.0
 * @see Util
 *
 * @author hollandjake (https://github.com/hollandjake)
 * @author gabrielwawerski (https://github.com/gabrielwawerski)
 */
public final class Utils {
    private static final Random RANDOM = new Random();

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
    public static final DateTimeFormatter ERROR_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");

    public static final String EMOJI_NEW_BUTTON = "\uD83C\uDD95";       // 🆕
    public static final String EMOJI_OK_BUTTON = "\uD83C\uDD97";        // 🆗
    public static final String EMOJI_PUSHPIN = "\uD83D\uDCCC";          // 📌
    public static final String EMOJI_SHUFFLE = "\uD83D\uDD00";          // 🔀
    public static final String EMOJI_INFRMATION = "\u2139\ufe0f";             // ℹ️
    public static final String EMOJI_MAGNIFYING = "\uD83D\uDD0E"; // 🔎️
    public static final String EMOJI_HOURGLASS = "\u23f3";             // ⏳
    public static final String EMOJI_EXCLAM_MRK = "\u2757";      // ❗
    public static final String EMOJI_POPCORN = "\uD83C\uDF7F";      // 🍿

    public static String getMatch(Message message, List<String> regexList) {
        String messageBody = message.getMessage();
        for (String regex : regexList) {
            if (messageBody.matches(regex)) {
                return regex;
            }
        }
        return "";
    }

    /**
     * Returns an ArrayList, containing regexes with their original form. (i think)
     *
     * @param regexList
     * @author hollandjake
     */
    public static ArrayList<String> getCommands(List<String> regexList) {
        return (ArrayList<String>) regexList.stream().map(Util::DEACTIONIFY).collect(Collectors.toList());
    }

    /**
     * Always make sure to "TO_REGEX"
     *
     * @param arg
     * @return
     */
    public static final String TO_REGEX(String arg) {
        return "(?i)^!\\s*" + arg + "$";
    }

    public static final String TO_COMMAND(String regex) {
        return regex.replaceAll("\\(\\?i\\)\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
    }

    static <T> T getRandom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}
