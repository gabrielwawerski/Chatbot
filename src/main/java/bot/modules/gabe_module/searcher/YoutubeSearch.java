package bot.modules.gabe_module.searcher;

import bot.core.Chatbot;
import bot.utils.gabe_modules.module_library.search.SimpleSearchModule;

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
