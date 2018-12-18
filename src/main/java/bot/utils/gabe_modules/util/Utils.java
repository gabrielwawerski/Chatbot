package bot.utils.gabe_modules.util;

import bot.core.helper.interfaces.Module;
import bot.core.helper.interfaces.Util;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

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

    /**
     * Always make sure to "actionify"
     *
     * @param arg
     * @return
     */
    public static final String actionify(String arg) {
        return "(?i)^!\\s*" + arg + "$";
    }

    public static final String deactionify(String regex) {
        return regex.replaceAll("\\(\\?i\\)\\^!\\\\\\\\s\\*(\\S+?)\\$", "$1");
    }

    static <T> T getRandom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}
