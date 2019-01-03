package bot.core.gabes_framework.core;

import bot.core.gabes_framework.core.Users;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.hollandjake_api.helper.misc.Message;

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
 * @see bot.core.hollandjake_api.helper.interfaces.Util
 *
 * @author hollandjake (https://github.com/hollandjake)
 * @author gabrielwawerski (https://github.com/gabrielwawerski)
 */
public final class Utils {
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
    public static final DateTimeFormatter ERROR_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
    private static final Random RANDOM = new Random();

    //region points
    public static final int POINTS_MAX_CHAR_20 = 1;
    public static final int POINTS_MAX_CHAR_60 = 2;
    public static final int POINTS_MAX_CHAR_100 = 5;
    public static final int POINTS_MAX_CHAR_300 = 10;

    public static final int POINTS_URL = 2;
    public static final int POINTS_IMAGE = 3;

    public static final int POINTS_B_INFO_REGEX = 1;
    public static final int POINTS_B_REGEX = 2;

    public static final int POINTS_EIGHTBALL_REGEX = 2;

    public static final int POINTS_POPCORN_REGEX = 2;

    public static final int POINTS_KARTAPULAPKA_REGEX = 2;

    public static final int POINTS_FEATURESUGGEST_INFO_REGEX = 1;
    public static final int POINTS_FEATURESUGGEST_REGEX = 3;

    public static final int POINTS_GOOGLESEARCH_INFO_REGEX = 1;
    public static final int POINTS_GOOGLESEARCH_REGEX = 2;

    public static final int POINTS_INFO_REGEX = 1;
    public static final int POINTS_STATS_REGEX = 1;
    public static final int POINTS_UPTIME_REGEX = 1;
    public static final int POINTS_ECHO_REGEX = 0;

    public static final int POINTS_JEBACLEZE_REGEX = 2;

    public static final int POINTS_MULTITORRENTSEARCH_REGEX = 3;

    public static final int POINTS_PYSZNE_INFO_REGEX = 1;
    public static final int POINTS_PYSZNE_RESTAURANT = 1;
    public static final int POINTS_PYSZNE_REGEX = 2;

    public static final int POINTS_RANDOMGROUPPHOTO_REGEX = 5;
    public static final int POINTS_RANDOMKWEJK_REGEX = 3;
    public static final int POINTS_RANDOMWTF_REGEX = 4;

    public static final int POINTS_ROLL_SIMPLE_REGEX = 2;
    public static final int POINTS_ROLL_REGEX = 3;

    public static final int POINTS_SIMPLESEARCHMODULE_REGEX = 2;

    public static final int POINTS_SIMPLEWEATHER_REGEX = 5;

    public static final int POINTS_TWITCHEMOTES_REGEX = 3;
    //endregion

    //region emojis
    /** 🆕 */
    public static final String NEW_BUTTON_EMOJI = "\uD83C\uDD95";
    /** 🆗 */
    public static final String OK_BUTTON_EMOJI = "\uD83C\uDD97";
    /** 📌 */
    public static final String PUSHPIN_EMOJI = "\uD83D\uDCCC";
    /** 🔀 */
    public static final String SHUFFLE_EMOJI = "\uD83D\uDD00";
    /** ℹ️ */
    public static final String INFO_EMOJI = "\u2139\ufe0f";
    /** 🔎️ */
    public static final String MAGNIFYING_EMOJI = "\uD83D\uDD0E";
    /** ⏳ */
    public static final String HOURGLASS_EMOJI = "\u23f3";
    /** ❗ */
    public static final String EXCL_MARK_RED_EMOJI = "\u2757";
    /** ❕ */
    public static final String EXCL_MARK_WHITE_EMOJI = "\u2755";
    /** 🍿 */
    public static final String POPCORN_EMOJI = "\uD83C\uDF7F";
    /** */
    public static final String RIGHT_ARRW_EMOJI = "\u27a1\ufe0f";
    /** */
    public static final String PENCIL_EMOJI = "\u270f\ufe0f";
    /** */
    public static final String THINK_EMOJI = "\uD83E\uDD14";
    /** */
    public static final String CAMERA_EMOJI = "\uD83D\uDCF8";
    /** */
    public static final String B_EMOJI = "\uD83C\uDD71️";
    //endregion

    public static String getMatch(Message message, List<String> regexList) {
        String messageBody = message.getMessage();
        for (String regex : regexList) {
            if (messageBody.matches(regex)) {
                return regex;
            }
        }
        return "";
    }

    public static boolean msgBy(Message message, Users users) {
        return message.getSender().getName().equals(users.getUserName());
    }

    public static ArrayList<String> getCommands(List<String> regexList) {
        return (ArrayList<String>) regexList.stream().map(bot.core.hollandjake_api.helper.interfaces.Util::DEACTIONIFY).collect(Collectors.toList());
    }

    public static ArrayList<String> getCommands(String... commands) {
        ArrayList<String> returnList = new ArrayList<>(commands.length);

        for (String x : commands) {
            returnList.add(TO_COMMAND(x));
        }
        return returnList;
    }

    public static String TO_REGEX(String arg) {
        return "(?i)^!\\s*" + arg + "$";
    }

    public static String TO_COMMAND(String regex) {
        return regex.replaceAll("\\(\\?i\\)\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
    }

    static <T> T getRandom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}
