package bot.core.gabes_framework.core.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @since 0.3313
 */
public final class Emoji {
    /** 🆕 */
    public static final String NEW_BUTTON = "\uD83C\uDD95";
    /** 🆗 */
    public static final String OK_BUTTON = "\uD83C\uDD97";
    /** 📌 */
    public static final String PUSHPIN = "\uD83D\uDCCC";
    /** 🔀 */
    public static final String SHUFFLE = "\uD83D\uDD00";
    /** ℹ️ */
    public static final String INFO = "\u2139\ufe0f";
    /** 🔎️ */
    public static final String MAGNIFYING = "\uD83D\uDD0E";
    /** 🍿 */
    public static final String POPCORN = "\uD83C\uDF7F";
    /** ➡️ */
    public static final String RIGHT_ARROW = "\u27a1\ufe0f";
    /** ✏️ */
    public static final String PENCIL = "\u270f\ufe0f";
    /** 🤔 */
    public static final String THINK = "\uD83E\uDD14";
    /** 📸 */
    public static final String CAMERA = "\uD83D\uDCF8";
    /** 🅱 */
    public static final String B = "\uD83C\uDD71️";
    /** ⏳ */
    public static final String HOURGLASS = "\u23f3";
    /** ❗ */
    public static final String EXCL_MARK_RED = "\u2757";
    /** 💯 */
    public static final String _100 = "\uD83D\uDCAF";

    public static ArrayList<String> getAll() {
        ArrayList<String> emojis = new ArrayList<>();

        for (Field f : Emoji.class.getDeclaredFields()) {
            try {
                emojis.add((String) f.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return emojis;
    }
}
