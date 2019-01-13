package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.helper.simple.SimpleSearchModule;

import java.util.List;

public class YoutubeSearch extends SimpleSearchModule {
    private static final String SEARCH_URL = "youtube.com/results?search_query=";

    public YoutubeSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of("youtube", "yt");
    }

    @Override
    protected String getSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String getSeparator() {
        return DEFAULT_WORD_SEPARATOR;
    }
}
