package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.simple.SimpleSearchModule;

import java.util.List;

/**
 * @version 1.0
 * @since 0.30 (18.12.2018)
 * @author Gabe
 */
public class AllegroSearch extends SimpleSearchModule {
    private static final String SEARCH_URL = "https://allegro.pl/listing?string=";
    private static final String SEPARATOR = "%20";

    public AllegroSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of("allegro", "all", "al");
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
