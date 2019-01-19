package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Config;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.search.SearchModuleBase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.gabes_framework.core.util.Utils.TO_REGEX;

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

    private static final String HELP_REGEX = TO_REGEX("pyszne help");
    private static final String HELP_2_REGEX = TO_REGEX("pyszne");

    private static final String SEARCH_REGEX = TO_REGEX("pyszne (.*)");

    // fixme all restaurants direct links are not working properly
    private static final String HAIANH_REGEX_1 = TO_REGEX("pyszne haianh");
    private static final String HAIANH_REGEX_2 = TO_REGEX("pyszne hai-anh");
    private static final String HAIANH_REGEX_3 = TO_REGEX("pyszne hai");

    private static final String MARIANO_ITALIANO_1 = TO_REGEX("pyszne mariano");
    private static final String MARIANO_ITALIANO_2 = TO_REGEX("pyszne italiano");

    private static final String FOOTBALL_PIZZA_1 = TO_REGEX("pyszne football");
    private static final String FOOTBALL_PIZZA_2 = TO_REGEX("pyszne footbal");
    private static final String FOOTBALL_PIZZA_3 = TO_REGEX("pyszne footballpizza");


    public PyszneSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(
                HELP_REGEX,
                HELP_2_REGEX,
                SEARCH_REGEX,
                HAIANH_REGEX_1,
                HAIANH_REGEX_2,
                HAIANH_REGEX_3,
                MARIANO_ITALIANO_1,
                MARIANO_ITALIANO_2,
                FOOTBALL_PIZZA_1,
                FOOTBALL_PIZZA_2,
                FOOTBALL_PIZZA_3);
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
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (isOr(HELP_REGEX, HELP_2_REGEX) ) {
            addPoints(message, Config.POINTS_PYSZNE_INFO_REGEX);
            chatbot.sendMessage(
                    "Restauracje dla kodu pocztowego."
                            + "\nAT THE GABES:"
                            + "\npyszne.pl/restauracja-lublin-lublin-20-455"
                            + "\n---"
                            + "\n!pyszne mariano  /" + " italiano"
                            + "\n!pyszne football   /" + " footballpizza"
                            + "\n!pyszne hai           /" + " hai-anh / haianh"
                            + "\n\n!pyszne kod pocztowy"
                            + "\nFormat: 00-000 lub 00 000");
            return true;

        } else if (isOr(MARIANO_ITALIANO_1, MARIANO_ITALIANO_2)) {
            addPoints(message, Config.POINTS_PYSZNE_RESTAURANT);
            chatbot.sendMessage("pyszne.pl/pizzeria-mariano-italiano");
            return true;

        } else if (is(HAIANH_REGEX_1, HAIANH_REGEX_2, HAIANH_REGEX_3)) {
            addPoints(message, Config.POINTS_PYSZNE_RESTAURANT);
            chatbot.sendMessage("pyszne.pl/bar-azjatycki-hai-ahn");
            return true;

        } else if (is(FOOTBALL_PIZZA_1, FOOTBALL_PIZZA_2, FOOTBALL_PIZZA_3)) {
            addPoints(message, Config.POINTS_PYSZNE_RESTAURANT);
            chatbot.sendMessage("pyszne.pl/football-pizza");
            return true;
        }

        updateMatcher(messageBody);
        if (is(SEARCH_REGEX)) {
            if (matchFound()) {
                if (analyzeMessage(messageBody)) {
                    addPoints(message, Config.POINTS_PYSZNE_REGEX);
                    chatbot.sendMessage(getFinalMessage());
                    return true;
                } else {
                    chatbot.sendMessage("Nieprawidłowy kod pocztowy.");
                    return true;
                }
            }
        }
        return false;
    }

    private void sendMessage(String message) {
        chatbot.sendMessage(getFinalMessage());
    }

    private boolean analyzeMessage(String messageBody) {
        String digit = "(\\d)";    // Any Single Digit 1
        String hyphen = "(-)";    // Any Single Character 1
        String space = "( )";    // Any Single Character 1

        Pattern pattern1 = Pattern.compile(digit + digit + hyphen + digit + digit + digit, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Pattern pattern2 = Pattern.compile(digit + digit + space + digit + digit + digit, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Pattern pattern3 = Pattern.compile(digit + digit + digit + digit + digit + digit, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

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
