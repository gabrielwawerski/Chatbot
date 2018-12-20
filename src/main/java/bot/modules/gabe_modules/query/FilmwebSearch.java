package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.gabes_framework.search.SimpleSearchModule;

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
