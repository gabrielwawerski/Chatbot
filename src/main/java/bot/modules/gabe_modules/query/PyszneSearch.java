package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.search.SearchModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @version 1.0

 * @since 0.30 (18.12.2018)
 */
public class PyszneSearch extends SearchModuleBase {
    private static final String SEARCH_URL = "pyszne.pl/restauracja-lublin-lublin-";
    private static final String SEPARATOR = "-";

    private final String PYSZNE_HELP_REGEX = makeRegex("pyszne help");
    private final String PYSZNE_ANY_REGEX = makeRegex("pyszne (.*)");

    public PyszneSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (match.equals(PYSZNE_HELP_REGEX)) {
            chatbot.sendMessage("Po komendzie wpisz kod pocztowy w formacie XX-XXX");
            return true;
        } else if (match.equals(PYSZNE_ANY_REGEX) ) {
            updateMatcher(messageBody);

            if (isMatchFound()) {
                if (analyzeMessage(messageBody)) {
                    chatbot.sendMessage(getFinalMessage(messageBody));
                    return true;
                } else {
                    chatbot.sendMessage("Nieprawidłowy kod pocztowy.");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(PYSZNE_HELP_REGEX)) {
            return PYSZNE_HELP_REGEX;
        } else if (messageBody.matches(PYSZNE_ANY_REGEX)) {
            return PYSZNE_ANY_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.deactionify(PYSZNE_ANY_REGEX));
        commands.add(Utils.deactionify(PYSZNE_HELP_REGEX));
        return commands;
    }

    private boolean analyzeMessage(String messageBody) {
        String re1 = "(\\d)";    // Any Single Digit 1
        String re2 = "(\\d)";    // Any Single Digit 2
        String re3 = "(-)";    // Any Single Character 1
        String re4 = "(\\d)";    // Any Single Digit 3
        String re5 = "(\\d)";    // Any Single Digit 4
        String re6 = "(\\d)";    // Any Single Digit 5

        Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5 + re6, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(messageBody);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
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
