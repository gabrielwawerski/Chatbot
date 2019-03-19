package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.search.SearchModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class WikipediaSearch extends SearchModuleBase {
    protected static final String SEARCH_URL = "https://pl.wikipedia.org/wiki/";
    protected static final String SEPARATOR = "_";

    public WikipediaSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        updateMatcher(message.getMessage());

        if (isRegex()) {
            if (matchFound()) {
                setUserMessage(capitalizeWord(getUserMessage().trim()));
                // capitalizes each word - wiki responds to that query better it seems
                chatbot.sendMessage(getFinalMessage());
                return true;
            }
        }
        return false;
    }

    private String capitalizeWord(String str){
        return Character.toString(str.charAt(0)).toUpperCase() + str.substring(1);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(Utils.TO_REGEX("wiki (.*)"), "wikipedia (.*)");
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
