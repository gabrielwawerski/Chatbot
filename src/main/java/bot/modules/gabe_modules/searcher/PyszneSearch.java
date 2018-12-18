package bot.modules.gabe_modules.searcher;

import bot.Chatbot;
import bot.impl.orig_impl.exceptions.MalformedCommandException;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.gabes_framework.search.SearchModuleBase;
import bot.impl.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @since 0.30 (18.12.2018)
 */
public class PyszneSearch extends SearchModuleBase {
    private static final String SEARCH_URL = "pyszne.pl/restauracja-lublin-lublin-";
    private static final String SEPARATOR = "-";

    private static final String PYSZNE_HELP_REGEX = makeRegex("pyszne|help");
    private static final String PYSZNE_ANY_REGEX = makeRegex("pyszne (.*)");

    public PyszneSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
//        updateMatch(message);
//        String messageBody = message.getMessage();
//        String msgToAnalyze = null;
//
//        if (msgToAnalyze.length() < 6 || msgToAnalyze.length() > 6) { // CHECK IF WORKS PROPERLY
//            chatbot.sendMessage("Nieprawidłowy kod pocztowy.");
//            return true; // TODO CHECK WHAT HAPPENS WHEN RETURNING FALSE
//
//        } else if (!analyzeMessage(msgToAnalyze)) {
//            chatbot.sendMessage("Nieprawidłowy kod pocztowy.");
//            return true;
//        } else {
//            chatbot.sendMessage(getFinalMessage(messageBody));
//        }
//
//        for (String regex : regexList) {
//            if (match.equals(regex)) {
//                updateMatcher(messageBody);
//
//                if (isMatchFound()) {
//                    chatbot.sendMessage(getFinalMessage(messageBody));
//                    return true;
//                } else {
//                    throw new MalformedCommandException();
//                }
//            }
//        }
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
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.deactionify(PYSZNE_ANY_REGEX));
        commands.add(Utils.deactionify(PYSZNE_HELP_REGEX));
    }

    private boolean analyzeMessage(String messageBody) {
        return Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(0))).find()      // Yx-xxx
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(1))).find()  // xY-xxx
                && (messageBody.charAt(2) == '-' && messageBody.charAt(2) == ' ')                      // - lub spacja
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(3))).find()  // xx-Yxx
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(4))).find()  // xx-xYx
                && Pattern.compile("[0-9]").matcher(Character.toString(messageBody.charAt(5))).find(); // xx-xxY
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
