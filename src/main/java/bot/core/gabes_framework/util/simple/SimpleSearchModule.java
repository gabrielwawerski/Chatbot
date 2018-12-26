package bot.core.gabes_framework.util.simple;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.gabes_framework.util.search.SearchModuleBase;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @since 0.30
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
    public String getMatch(Message message) {
        return Utils.getMatch(message, regexList); // check if working
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(regexList);
    }
}
