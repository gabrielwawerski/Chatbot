package bot.modules.gabe.search;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.gabes_framework.util.search.SearchModuleBase;
import bot.core.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @since 0.30
 */
public class GoogleSearch extends SearchModuleBase {
    private static final String SEARCH_URL = "https://www.google.com/search?q=";
    private static final String SEPARATOR = "+";

    private final String GOOGLE_REGEX = Util.ACTIONIFY("google");
    private final String G_REGEX = Util.ACTIONIFY("g");

    private final String G_HELP_REGEX = Util.ACTIONIFY("g help");
    private final String G_LEZE_REGEX = Util.ACTIONIFY("g leze");

    private final String GOOGLE_ANY_REGEX = Util.ACTIONIFY("google (.*)");
    private final String G_ANY_REGEX = Util.ACTIONIFY("g (.*)");

    public GoogleSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String msg = message.getMessage();

        if (match.equals(GOOGLE_REGEX) || match.equals(G_REGEX) || match.equals(G_HELP_REGEX)) {
            chatbot.sendMessage("Jak coś przetłumaczyć?\n"
                    + "!g <tekst> translate *język*\n"
                    + "Języki: en, pl, ...");
            return true;

        } else if (match.equals(G_LEZE_REGEX)) {
            List<String> list = List.of("POSZUKAJ KUUUURRWAAAAA",
                    "Masz ty inwalido umysłowy",
                    "Weź, kurwa, poszukaj.",
                    "No kurwa, łeze, poszukaj",
                    "KKUUUUUUURRRRRRWWW",
                    "Ty ulana łysa lebiodo");
            chatbot.sendMessage(Util.GET_RANDOM(list) +
                    "\nhttps://www.google.com/");
            return true;

        } else if (match.equals(GOOGLE_ANY_REGEX) || match.equals(G_ANY_REGEX)) {
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

        if (messageBody.matches(GOOGLE_REGEX)) {
            return GOOGLE_REGEX;
        } else if (messageBody.matches(G_REGEX)) {
            return G_REGEX;
        } else if (messageBody.matches(G_LEZE_REGEX)) {
            return G_LEZE_REGEX;
        } else if (messageBody.matches(G_HELP_REGEX)) {
            return G_HELP_REGEX;
        } else if (messageBody.matches(GOOGLE_ANY_REGEX)) {
            return GOOGLE_ANY_REGEX;
        } else if (messageBody.matches(G_ANY_REGEX)) {
            return G_ANY_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.TO_COMMAND(GOOGLE_REGEX));
        commands.add(Utils.TO_COMMAND(G_REGEX));
        commands.add(Utils.TO_COMMAND(G_HELP_REGEX));
        commands.add(Utils.TO_COMMAND(G_LEZE_REGEX));
        commands.add(Utils.TO_COMMAND(GOOGLE_ANY_REGEX));
        commands.add(Utils.TO_COMMAND(G_ANY_REGEX));
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
