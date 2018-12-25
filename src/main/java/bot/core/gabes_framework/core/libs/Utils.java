package bot.core.gabes_framework.core.libs;

import bot.core.gabes_framework.core.point_system.Users;
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

    public static final String NEW_BUTTON_EMOJI = "\uD83C\uDD95"; // 🆕
    public static final String OK_BUTTON_EMOJI = "\uD83C\uDD97"; // 🆗
    public static final String PUSHPIN_EMOJI = "\uD83D\uDCCC"; // 📌
    public static final String SHUFFLE_EMOJI = "\uD83D\uDD00"; // 🔀
    public static final String INFRMATION_EMOJI = "\u2139\ufe0f"; // ℹ️
    public static final String MAGNIFYING_EMOJI = "\uD83D\uDD0E"; // 🔎️
    public static final String HOURGLASS_EMOJI = "\u23f3"; // ⏳
    public static final String EXCLAM_MRK_EMOJI = "\u2757"; // ❗
    public static final String POPCORN_EMOJI = "\uD83C\uDF7F"; // 🍿
    public static final String RIGHT_ARRW_EMOJI = "\u27a1\ufe0f";
    public static final String PENCIL_EMOJI = "\u270f\ufe0f";
    public static final String THINK_EMOJI = "\uD83E\uDD14";
    public static final String CAMERA_EMOJI = "\uD83D\uDCF8";

    public static String getMatch(Message message, List<String> regexList) {
        String messageBody = message.getMessage();
        for (String regex : regexList) {
            if (messageBody.matches(regex)) {
                return regex;
            }
        }
        return "";
    }

    public static boolean msgIsBy(Message message, Users user) {
        return message.getSender().getName().equals(user.name());
    }

    public static ArrayList<String> getCommands(List<String> regexList) {
        return (ArrayList<String>) regexList.stream().map(Util::DEACTIONIFY).collect(Collectors.toList());
    }

    public static ArrayList<String> getCommands(String... commands) {
        ArrayList<String> returnList = new ArrayList<>(commands.length);

        for (String x : commands) {
            returnList.add(TO_COMMAND(x));
        }
        return returnList;
    }

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
