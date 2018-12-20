package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.search.SearchModuleBase;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.search.SimpleSearchModule;

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

    private final String HELP_REGEX = makeRegex("pyszne help");
    private final String ANY_REGEX = makeRegex("pyszne (.*)");

    private final String HAIANH_REGEX_1 = makeRegex("pyszne haianhp");
    private final String HAIANH_REGEX_2 = makeRegex("pyszne hai-anh");

    private final String MARIANO_ITALIANO_1 = makeRegex("pyszne mariano");
    private final String MARIANO_ITALIANO_2 = makeRegex("pyszne italiano");

    private final String FOOTBALL_PIZZA_1 = makeRegex("pyszne football");
    private final String FOOTBALL_PIZZA_2 = makeRegex("pyszne footbal");
    private final String FOOTBALL_PIZZA_3 = makeRegex("pyszne footballpizza");


    public PyszneSearch(Chatbot chatbot) {
        super(chatbot);
    }

    private void sendMessage(String message) {
        chatbot.sendMessage(getFinalMessage(message));
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (match.equals(HELP_REGEX)) {
            chatbot.sendMessage("Po komendzie wpisz kod pocztowy w formacie XX-XXX");
            return true;
        } else if (match.equals(MARIANO_ITALIANO_1) || match.equals(MARIANO_ITALIANO_2)) {
            sendMessage(messageBody);
            return true;
        } else if (match.equals(HAIANH_REGEX_1) || match.equals(HAIANH_REGEX_2)) {
            sendMessage(messageBody);
            return true;
        } else if (match.equals(match.equals(FOOTBALL_PIZZA_1)) || match.equals(FOOTBALL_PIZZA_2) || match.equals(FOOTBALL_PIZZA_3)) {
            sendMessage(messageBody);
            return true;
        }

        System.out.println("false");
        updateMatcher(messageBody);
        if (match.equals(ANY_REGEX) ) {
//            updateMatcher(messageBody);

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

        if (messageBody.matches(HELP_REGEX)) {
            return HELP_REGEX;
        } else if (messageBody.matches(HAIANH_REGEX_1)) {
            return HAIANH_REGEX_1;
        } else if (messageBody.matches(HAIANH_REGEX_2)) {
            return HAIANH_REGEX_2;
        } else if (messageBody.matches(MARIANO_ITALIANO_1)) {
            return MARIANO_ITALIANO_1;
        } else if (messageBody.matches(MARIANO_ITALIANO_2)) {
            return MARIANO_ITALIANO_2;
        } else if (messageBody.matches(FOOTBALL_PIZZA_1)) {
            return FOOTBALL_PIZZA_1;
        } else if (messageBody.matches(FOOTBALL_PIZZA_2)) {
            return FOOTBALL_PIZZA_3;
        } else if (messageBody.matches(FOOTBALL_PIZZA_3)) {
            return FOOTBALL_PIZZA_3;
        } else if (messageBody.matches(ANY_REGEX)) {
            return ANY_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.deactionify(HAIANH_REGEX_1));
        commands.add(Utils.deactionify(HAIANH_REGEX_2));
        commands.add(Utils.deactionify(FOOTBALL_PIZZA_1));
        commands.add(Utils.deactionify(FOOTBALL_PIZZA_2));
        commands.add(Utils.deactionify(FOOTBALL_PIZZA_3));
        commands.add(Utils.deactionify(MARIANO_ITALIANO_1));
        commands.add(Utils.deactionify(MARIANO_ITALIANO_2));
        commands.add(Utils.deactionify(ANY_REGEX));
        commands.add(Utils.deactionify(HELP_REGEX));
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
