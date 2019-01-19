package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Config;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.search.SearchModuleBase;
import bot.core.gabes_framework.core.util.Utils;

import java.util.List;

/**
 * @version 1.0
 * @since 0.30
 */
public class GoogleSearch extends SearchModuleBase {
    private static final String SEARCH_URL = "https://www.google.com/search?q=";
    private static final String SEPARATOR = "+";

    private static final String GOOGLE_REGEX = Utils.TO_REGEX("google");
    private static final String G_REGEX = Utils.TO_REGEX("g");

    private static final String G_HELP_REGEX = Utils.TO_REGEX("g help");
    private static final String G_LEZE_REGEX = Utils.TO_REGEX("g leze");

    private static final String GOOGLE_ANY_REGEX = Utils.TO_REGEX("google (.*)");
    private static final String G_ANY_REGEX = Utils.TO_REGEX("g (.*)");

    public GoogleSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String msg = message.getMessage();

        if (is(GOOGLE_REGEX, G_REGEX, G_HELP_REGEX)) {
            addPoints(message, Config.POINTS_GOOGLESEARCH_INFO_REGEX);
            chatbot.sendMessage("Jak coś przetłumaczyć?\n"
                    + "!g <tekst> translate *język*\n"
                    + "Języki: en, pl, ...");
            return true;

        } else if (is(G_LEZE_REGEX)) {
            List<String> list = List.of("POSZUKAJ KUUUURRWAAAAA",
                    "Masz ty inwalido umysłowy",
                    "Weź, kurwa, poszukaj.",
                    "No kurwa, łeze, poszukaj",
                    "KKUUUUUUURRRRRRWWW",
                    "Ty ulana łysa lebiodo");
            chatbot.sendMessage(bot.core.hollandjake_api.helper.interfaces.Util.GET_RANDOM(list) +
                    "\nhttps://www.google.com/");
            return true;

        } else if (isOr(GOOGLE_ANY_REGEX, G_ANY_REGEX)) {
            updateMatcher(msg);

            if (matchFound()) {
                addPoints(message, Config.POINTS_GOOGLESEARCH_REGEX);
                chatbot.sendMessage(getFinalMessage());
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    protected String getSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String getSeparator() {
        return SEPARATOR;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(
                GOOGLE_REGEX,
                G_REGEX,
                G_HELP_REGEX,
                G_LEZE_REGEX,
                GOOGLE_ANY_REGEX,
                G_ANY_REGEX);
    }
}
