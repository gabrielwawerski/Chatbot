package bot.modules.gabe_module.searcher;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.utils.gabe_modules.module_library.search.SearchModule;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @since 0.30 (18.12.2018)
 */
public class PyszneSearch extends SearchModule {
    private static final String SEARCH_URL = "pyszne.pl/restauracja-lublin-lublin-";
    private static final String SEPARATOR = "-";

    public PyszneSearch(Chatbot chatbot, List<String> regexes) {
        super(chatbot, regexes);
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
//            chatbot.sendMessage(getFinalUrl(messageBody));
//        }
//
//        for (String regex : regexList) {
//            if (match.equals(regex)) {
//                setMatcher(messageBody);
//
//                if (isMatchFound()) {
//                    chatbot.sendMessage(getFinalUrl(messageBody));
//                    return true;
//                } else {
//                    throw new MalformedCommandException();
//                }
//            }
//        }
        return false;
    }

    private boolean analyzeMessage(String message) {
        return
                   Pattern.compile("[0-9]").matcher(Character.toString(message.charAt(0))).find()  // Yx-xxx
                && Pattern.compile("[0-9]").matcher(Character.toString(message.charAt(1))).find()  // xY-xxx
                && (message.charAt(2) == '-' && message.charAt(2) == ' ')                          // - lub spacja
                && Pattern.compile("[0-9]").matcher(Character.toString(message.charAt(3))).find()  // xx-Yxx
                && Pattern.compile("[0-9]").matcher(Character.toString(message.charAt(4))).find()  // xx-xYx
                && Pattern.compile("[0-9]").matcher(Character.toString(message.charAt(5))).find(); // xx-xxY
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
