package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.libs.Utils;
import bot.gabes_framework.search.SearchModuleBase;

import java.util.ArrayList;
import java.util.List;

public class TorrentTest extends SearchModuleBase {
    private static final String SEARCH_URL = "rarbg.to/torrents.php?search=";
    private static final String POSTFIX = "/1/";
    private final String TORRENT_REGEX = Utils.ACTIONIFY("torrent (.*)");

    public TorrentTest(Chatbot chatbot, List<String> regexList) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();
        updateMatcher(messageBody);

        if (match.equals(TORRENT_REGEX)) {
                if (isMatchFound()) {
                    messageBody = matcher.group(1).replaceAll("\\s+", SEPARATOR);
                    chatbot.sendMessage(SEARCH_URL + messageBody + POSTFIX);
                    return true;
                } else {
                    chatbot.sendMessage("Coś żeś pojebał");
                    return true;
                }
            }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.equals(TORRENT_REGEX)) {
            return TORRENT_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.DEACTIONIFY(TORRENT_REGEX));
        return commands;
    }

    @Override
    protected String getFinalMessage(String messageBody) {
        return SEARCH_URL + toQuery(messageBody) + POSTFIX;
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
