package bot.gabes_framework.search;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.List;
/**
 * Provides a quick way of creating modules which will return URL with user query. Extend if the process is as simple as
 * adding the user query to predefined URL. Example of such URL: {@code https://www.google.com/search?q=}
 *
 * <p>You can change the default separator (see {@linkplain #DEFAULT_SEPARATOR}) by overriding {@link #setSeparator()}
 * and setting it there.
 *
 * Replaces spaces between words with a plus sign.
 * @since 0.30
 * @version 1.0
 *
 * @see SearchModuleBase
 */
public abstract class SimpleSearchModule extends SearchModuleBase {
    protected List<String> regexList;

    public SimpleSearchModule(Chatbot chatbot, List<String> regexList) {
        super(chatbot);
        this.regexList = getMappedRegexList(regexList);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        for (String regex : regexList) {
            if (match.equals(regex)) {
                updateMatcher(messageBody);

                if (isMatchFound()) {
                    chatbot.sendMessage(getFinalMessage(messageBody));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String setSeparator() {
        return DEFAULT_SEPARATOR;
    }

    @Override
    public String getMatch(Message message) {
        return Utils.getMatch(message, regexList); // check if working
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(regexList);
    }
}
