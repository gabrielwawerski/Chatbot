package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.search.SimpleSearchModule;
import org.apache.commons.lang.WordUtils;

import java.util.List;

public class WikipediaSearch extends SimpleSearchModule {
    protected static final String SEARCH_URL = "https://pl.wikipedia.org/wiki/";
    protected static final String SEPARATOR = "_";

    public WikipediaSearch(Chatbot chatbot, List<String> regexList) {
        super(chatbot, regexList);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        for (String regex : regexList) {
            if (match.equals(regex)) {
                updateMatcher(messageBody);

                if (isMatchFound()) {
                    // capitalizes each word - wiki responds to that query better it seems
                    chatbot.sendMessage(getFinalMessage(WordUtils.capitalizeFully(messageBody)));
                    return true;
                }
            }
        }
        return false;
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