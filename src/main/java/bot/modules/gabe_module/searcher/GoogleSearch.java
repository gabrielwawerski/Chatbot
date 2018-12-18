package bot.modules.gabe_module.searcher;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.utils.gabe_modules.module_library.search.SearchModuleBase;
import bot.utils.gabe_modules.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @since 0.30
 */
public class GoogleSearch extends SearchModuleBase {
    private static final String SEARCH_URL = "https://www.google.com/search?q=";
    private static final String SEPARATOR = "+";

    private final String GOOGLE_REGEX = Util.ACTIONIFY("google (.*)");
    private final String G_REGEX = Util.ACTIONIFY("g (.*)");

    private final String G_HELP_REGEX = Util.ACTIONIFY("g|help");
    private final String G_LEZE_REGEX = Util.ACTIONIFY("g|leze");

    public GoogleSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String msg = message.getMessage();

        if (match.equals(G_HELP_REGEX)) {
            chatbot.sendMessage("Jak coś przetłumaczyć?\n"
                    + "!g <tekst> translate *język*\n"
                    + "Języki: en, pl, ...");
            return true;

        } else if (match.equals(G_LEZE_REGEX)) {
            List<String> list = List.of("POSZUKAJ KUUUURRWAAAAA",
                    "Masz, ty inwalido umysłowy",
                    "No kurwa leze poszukaj sobie, nie masz google?",
                    "Ty ulana łysa lebiodo");
            chatbot.sendMessage(Util.GET_RANDOM(list) +
                    "\nhttps://www.google.com/");
            return true;

        } else if (match.equals(GOOGLE_REGEX) || match.equals(G_REGEX)) {
            updateMatcher(msg);

            if (isMatchFound()) {
                chatbot.sendMessage(getFinalMessage(msg));
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(G_HELP_REGEX)) {
            return G_HELP_REGEX;
        }
        if (messageBody.matches(G_LEZE_REGEX)) {
            return G_LEZE_REGEX;
        }
        if (messageBody.matches(GOOGLE_REGEX)) {
            return GOOGLE_REGEX;
        }
        if (messageBody.matches(G_REGEX)) {
            return G_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add(Utils.deactionify(GOOGLE_REGEX));
        commands.add(Utils.deactionify(G_REGEX));
        commands.add(Utils.deactionify(GOOGLE_REGEX));
        commands.add(Utils.deactionify(GOOGLE_REGEX));
        return commands;
    }

    @Override
    protected String setSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String setSeparator() {
        return SEPARATOR;
    }
}
