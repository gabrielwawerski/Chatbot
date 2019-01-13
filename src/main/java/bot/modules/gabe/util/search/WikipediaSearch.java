package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.simple.SimpleSearchModule;

import java.util.List;

public class WikipediaSearch extends SimpleSearchModule {
    protected static final String SEARCH_URL = "https://pl.wikipedia.org/wiki/";
    protected static final String SEPARATOR = "_";

    public WikipediaSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of("wiki", "wikipedia");
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        for (String regex : regexes) {
            if (match.equals(regex)) {
                updateMatcher(messageBody);

                if (matchFound()) {
                    // capitalizes each word - wiki responds to that query better it seems
                    chatbot.sendMessage(getFinalMessage());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected String getSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String getSeparator() {
        return SEPARATOR;
    }
}
