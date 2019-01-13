package bot.core.gabes_framework.framework.simple;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.gabes_framework.framework.search.SearchModuleBase;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.core.util.Utils;

/**
 * @version 1.1
 * @since 0.30
 */
public abstract class SimpleSearchModule extends SearchModuleBase {
    public SimpleSearchModule(Chatbot chatbot) {
        super(chatbot);
        this.regexes = getMappedSearchRegexes(regexes);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (isRegex()) {
            updateMatcher(messageBody);

            if (matchFound()) {
                addPoints(message, Utils.POINTS_SIMPLESEARCH_MODULE_REGEX);
                chatbot.sendMessage(getFinalMessage());
                return true;
            }
        }
        return false;
    }
}
