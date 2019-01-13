package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.simple.SimpleSearchModule;

import java.util.List;

public class FilmwebSearch extends SimpleSearchModule {
    private static final String SEARCH_URL = "filmweb.pl/search?q=";

    public FilmwebSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of("filmweb", "fw");
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
