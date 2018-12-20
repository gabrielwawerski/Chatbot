package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.search.SearchModuleBase;

import java.util.ArrayList;

public class TorrentSearch extends SearchModuleBase {
    private static final String X1337_URL = "https://1337x.to/search/";
    private static final String X1337_POSTFIX = "/1/";

    private static final String RARBG_URL = "rarbg.to/torrents.php?search=dexter+season+1";

    private static final String TORRENTZ_URL = "https://torrentz2.eu/search?f=dexter+season+1";

    public TorrentSearch(Chatbot chatbot) {
        super(chatbot);
    }


    @Override
    protected String setSearchUrl() {
        return null;
    }

    @Override
    protected String setSeparator() {
        return null;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return null;
    }

    @Override
    public ArrayList<String> getCommands() {
        return null;
    }
}
