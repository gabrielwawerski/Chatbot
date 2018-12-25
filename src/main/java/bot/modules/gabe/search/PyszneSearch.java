package bot.modules.gabe.search;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.search.SearchModuleBase;
import bot.core.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.libs.Utils.TO_REGEX;

/**
 * @version 1.0

 * @since 0.30 (18.12.2018)
 */
public class PyszneSearch extends SearchModuleBase {
    private static final String SEARCH_URL = "pyszne.pl/restauracja-lublin-lublin-";
    private static final String SEPARATOR = "-";

    private static final String FOOTBALL_PIZZA_URL = "https://www.pyszne.pl/football-pizza";
    private static final String MARIANO_ITALIANO_URL = "https://www.pyszne.pl/pizzeria-mariano-italiano";
    private static final String HAIANH_URL = "https://www.pyszne.pl/bar-azjatycki-hai-ahn";

    private final String HELP_REGEX = TO_REGEX("pyszne help");
    private final String HELP_2_REGEX = TO_REGEX("pyszne");

    private final String SEARCH_REGEX = TO_REGEX("pyszne (.*)");

    // fixme all restaurants direct links are not working properly
    private final String HAIANH_REGEX_1 = TO_REGEX("pyszne haianh");
    private final String HAIANH_REGEX_2 = TO_REGEX("pyszne hai-anh");
    private final String HAIANH_REGEX_3 = TO_REGEX("pyszne hai");

    private final String MARIANO_ITALIANO_1 = TO_REGEX("pyszne mariano");
    private final String MARIANO_ITALIANO_2 = TO_REGEX("pyszne italiano");

    private final String FOOTBALL_PIZZA_1 = TO_REGEX("pyszne football");
    private final String FOOTBALL_PIZZA_2 = TO_REGEX("pyszne footbal");
    private final String FOOTBALL_PIZZA_3 = TO_REGEX("pyszne footballpizza");


    public PyszneSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected String setSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String setSeparator() {
        return SEPARATOR;
    }

    private void sendMessage(String message) {
        chatbot.sendMessage(getFinalMessage(message));
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (match.equals(HELP_REGEX) || match.equals(HELP_2_REGEX) ) {
            chatbot.sendMessage(
                    "Restauracje dla kodu pocztowego."
                            + "\nAT THE GABES:"
                            + "\npyszne.pl/restauracja-lublin-lublin-20-455"
                            + "\n---"
                            + "\n!pyszne mariano  /" + " italiano"
                            + "\n!pyszne football   /" + " footballpizza"
                            + "\n!pyszne hai           /" + " hai-anh / haianh"
                            + "\n\n!pyszne kod pocztowy"
                            + "\nFormat: 00-000 lub 00 000 lub 00000");
            return true;

        } else if (match.equals(MARIANO_ITALIANO_1) || match.equals(MARIANO_ITALIANO_2)) {
            chatbot.sendMessage("pyszne.pl/pizzeria-mariano-italiano");
            return true;
        } else if (match.equals(HAIANH_REGEX_1) || match.equals(HAIANH_REGEX_2)
                || match.equals(HAIANH_REGEX_3)) {
            chatbot.sendMessage("pyszne.pl/bar-azjatycki-hai-ahn");
            return true;
        } else if (match.equals(FOOTBALL_PIZZA_1) || match.equals(FOOTBALL_PIZZA_2)
                || match.equals(FOOTBALL_PIZZA_3)) {
            chatbot.sendMessage("pyszne.pl/football-pizza");
            return true;
        }

        updateMatcher(messageBody);
        if (match.equals(SEARCH_REGEX)) {
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
        } else if (messageBody.matches(HELP_2_REGEX)) {
            return HELP_2_REGEX;
        } else if (messageBody.matches(HAIANH_REGEX_1)) {
            return HAIANH_REGEX_1;
        } else if (messageBody.matches(HAIANH_REGEX_2)) {
            return HAIANH_REGEX_2;
        } else if (messageBody.matches(HAIANH_REGEX_3)) {
            return HAIANH_REGEX_3;
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
        } else if (messageBody.matches(SEARCH_REGEX)) {
            return SEARCH_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.TO_COMMAND(HAIANH_REGEX_1));
        commands.add(Utils.TO_COMMAND(HAIANH_REGEX_2));
        commands.add(Utils.TO_COMMAND(HAIANH_REGEX_3));
        commands.add(Utils.TO_COMMAND(FOOTBALL_PIZZA_1));
        commands.add(Utils.TO_COMMAND(FOOTBALL_PIZZA_2));
        commands.add(Utils.TO_COMMAND(FOOTBALL_PIZZA_3));
        commands.add(Utils.TO_COMMAND(MARIANO_ITALIANO_1));
        commands.add(Utils.TO_COMMAND(MARIANO_ITALIANO_2));
        commands.add(Utils.TO_COMMAND(HELP_REGEX));
        commands.add(Utils.TO_COMMAND(HELP_2_REGEX));
        commands.add(Utils.TO_COMMAND(SEARCH_REGEX));
        return commands;
    }

    private boolean analyzeMessage(String messageBody) {
        String nr = "(\\d)";    // Any Single Digit 1
        String hyphen = "(-)";    // Any Single Character 1
        String space = "( )";    // Any Single Character 1

        Pattern pattern1 = Pattern.compile(nr + nr + hyphen + nr + nr + nr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Pattern pattern2 = Pattern.compile(nr + nr + space + nr + nr + nr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Pattern pattern3 = Pattern.compile(nr + nr + nr + nr + nr + nr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher;

        matcher = pattern1.matcher(messageBody);
        if (matcher.find()) {
            return true;
        }

        matcher = pattern2.matcher(messageBody);
        if (matcher.find()) {
            return true;
        }

        matcher = pattern3.matcher(messageBody);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}
