package bot.modules.gabe_module.searcher;

import bot.core.Chatbot;
import bot.utils.gabe_modules.module_library.search.SimpleSearchModule;

import java.util.List;

/**
 * @version 1.0
 * @since 0.30 (18.12.2018)
 * @author Gabe
 */
public class AllegroSearch extends SimpleSearchModule {
    private static final String SEARCH_URL = "https://allegro.pl/listing?string=";
    private static final String SEPARATOR = "%20";

    public AllegroSearch(Chatbot chatbot, List<String> regexes) {
        super(chatbot, regexes);
    }

    @Override
    protected String setSearchUrl() {
        return SEARCH_URL;
    }

    @Override
    protected String setSeparator() {
        return SEPARATOR;
    }
}
