package bot.core.gabes_framework.core.util;

import bot.core.gabes_framework.core.database.Users;
import bot.core.gabes_framework.core.database.DBConnection;
import bot.core.gabes_framework.core.database.User;
import bot.core.hollandjake_api.helper.misc.Message;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 1.1<br>
 * - moved emojis and points to separate classes
 *
 * @since 0.30
 * @version 1.1
 * @see bot.core.hollandjake_api.helper.interfaces.Util
 */
public final class Utils {
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm:ss");
    public static final DateTimeFormatter ERROR_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");
    private static final Random RANDOM = new Random();

    private static DBConnection db = DBConnection.getInstance();

    public static boolean msgBy(Message message, Users users) {
        return message.getSender().getName().equals(users.getUserName());
    }

    public static ArrayList<String> getCommands(List<String> regexes) {
        return (ArrayList<String>) regexes.stream().map(bot.core.hollandjake_api.helper.interfaces.Util::DEACTIONIFY).collect(Collectors.toList());
    }

    public static ArrayList<String> getCommands(String... regexes) {
        ArrayList<String> returnList = new ArrayList<>(regexes.length);

        for (String regex : regexes) {
            returnList.add(TO_COMMAND(regex));
        }
        return returnList;
    }

    /**
     * Transfers points from one user to another. Also refreshes and updates their database entries.
     *
     * @param to the user to transfer points to
     * @param from user to transfer points from
     * @param points amount of points to transfer
     */
    public static void transfer(User to, User from, int points) {
        db.refresh(to, from);
        to.addPoints(points);
        from.subPoints(points);
        db.update(to, from);
    }

    public static String TO_REGEX(String arg) {
        return "(?i)^!\\s*" + arg + "$";
    }

    // TODO TEST
    public static List<String> TO_REGEX(List<String> regexes) {
        ArrayList<String> returnList = new ArrayList<>(regexes.size());

        for (String regex : regexes) {
            returnList.add("(?i)^!\\s*" + regex + "$");
        }
        return returnList;
    }

    public static String TO_COMMAND(String regex) {
        return regex.replaceAll("\\(\\?i\\)\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
    }

    static <T> T getRandom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    public static boolean fiftyFifty() {
        int min = 0;
        int max = 1;
        int range = (max - min) + 1;

        int result = (int) (Math.random() * range) + min;
        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }
}
