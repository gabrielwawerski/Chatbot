package bot.modules.gabe_module.searcher;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.utils.gabe_modules.module_library.simple.SearchModuleBase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public String getMatch(Message message) {
        return null;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        Matcher matcher = null;

        if (match.equals(GOOGLE_REGEX)) {
            matcher = Pattern.compile(match).matcher(message.getMessage());

        } else if (match.equals(G_LEZE_REGEX)) {
            List list = List.of("POSZUKAJ KUUUURRWAAAAA",
                    "Masz, ty inwalido umysłowy",
                    "No kurwa leze poszukaj sobie, nie masz google?",
                    "Ty ulana łysa lebiodo");
            chatbot.sendMessage(Util.GET_RANDOM(list) +
                    "\nhttps://www.google.com/");
            return true;
        } else if (match.equals(G_HELP_REGEX)) {
            chatbot.sendMessage("Jak otrzymać link z tłumaczeniem?\n"
                            + "!g <tekst> translate *język*\n"
                            + "Języki: en, pl, ...");
            return true;
        } else {
            return false;
        }

        if (matcher.find()) {
            String msg = matcher.group(1);
            msg = msg.replaceAll("\\s+","+");
//            msg = msg.trim().replaceAll("\\s{2,}", "+").trim();
            chatbot.sendMessage(SEARCH_URL + msg);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add()
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
