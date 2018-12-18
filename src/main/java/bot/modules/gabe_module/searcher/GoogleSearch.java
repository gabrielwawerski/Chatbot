package bot.modules.gabe_module.searcher;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.utils.gabe_modules.util.module_library.SimpleModule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.helper.interfaces.Util.DEACTIONIFY;

/**
 * @author Gabe
 */
public class GoogleSearch extends SimpleModule {
    private final String SEARCH_URL = "https://www.google.com/search?q=";

    private final String G_HELP_REGEX = Util.ACTIONIFY("g help");

    private final String GOOGLE_REGEX = Util.ACTIONIFY("google (.*)");
    private final String G_REGEX = Util.ACTIONIFY("g (.*)");
    private final String SEARCH_REGEX = Util.ACTIONIFY("search (.*)");
    private final String S_REGEX = Util.ACTIONIFY("s (.*)");

    private final String G_LEZE_REGEX = Util.ACTIONIFY("g leze");



    public GoogleSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        Matcher matcher = null;

        if (match.equals(GOOGLE_REGEX)) {
            matcher = Pattern.compile(GOOGLE_REGEX).matcher(message.getMessage());
        } else if (match.equals(G_REGEX)) {
            matcher = Pattern.compile(G_REGEX).matcher(message.getMessage());
        } else if (match.equals(SEARCH_REGEX)) {
            matcher = Pattern.compile(SEARCH_REGEX).matcher(message.getMessage());
        } else if (match.equals(S_REGEX)) {
            matcher = Pattern.compile(S_REGEX).matcher(message.getMessage());
        } else if (match.equals(G_LEZE_REGEX)) {
            List list = List.of("POSZUKAJ KUUUURRWAAAAA",
                    "Masz, ty inwalido umysłowy",
                    "No kurwa leze poszukaj sobie, nie masz google?",
                    "Ty ulana łysa lebiodo");
            chatbot.sendMessage(Util.GET_RANDOM(list) +
                    "\nhttps://www.google.com/");
            return true;
        } else if (match.equals(G_HELP_REGEX)) {
            chatbot.sendMessage("Jak dostać link do tłumaczenia?\n" +
                    "!g <tekst> translate *language\n" +
                    "en, pl, ");
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
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(GOOGLE_REGEX)) {
            return GOOGLE_REGEX;
        } else if (messageBody.matches(G_REGEX)) {
            return G_REGEX;
        } else if (messageBody.matches(SEARCH_REGEX)){
            return SEARCH_REGEX;
        } else if (messageBody.matches(S_REGEX)) {
            return S_REGEX;
        } else {
            return "";
        }
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(GOOGLE_REGEX));
        commands.add(DEACTIONIFY(G_REGEX));
        commands.add(DEACTIONIFY(SEARCH_REGEX));
        commands.add(DEACTIONIFY(S_REGEX));
        return commands;
    }
}