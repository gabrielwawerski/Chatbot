package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.simple.SimpleSearchModule;

import java.util.List;

public class YoutubeSearch extends SimpleSearchModule {
    private static final String SEARCH_URL = "youtube.com/results?search_query=";

    public YoutubeSearch(Chatbot chatbot, List<String> regexes) {
        super(chatbot, regexes);
    }

    @Override
    protected String setSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String setSeparator() {
        return DEFAULT_SEPARATOR;
    }
}
