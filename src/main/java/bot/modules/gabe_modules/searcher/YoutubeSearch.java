package bot.modules.gabe_modules.searcher;

import bot.Chatbot;
import bot.impl.gabes_framework.search.SimpleSearchModule;

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
