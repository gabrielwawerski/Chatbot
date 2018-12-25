package bot.modules.gabe.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.simple.SimpleSearchModule;

import java.util.List;

public class FilmwebSearch extends SimpleSearchModule {
    private static final String SEARCH_URL = "filmweb.pl/search?q=";

    public FilmwebSearch(Chatbot chatbot, List<String> regexList) {
        super(chatbot, regexList);
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
