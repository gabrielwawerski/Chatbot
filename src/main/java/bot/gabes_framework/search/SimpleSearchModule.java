package bot.gabes_framework.search;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @since 0.30
 */
public abstract class SimpleSearchModule extends SearchModuleBase {
    private List<String> regexList;

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
