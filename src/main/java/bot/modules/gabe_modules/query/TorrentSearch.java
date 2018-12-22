package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentSearch extends ModuleBase {
    private final String SEARCH_REGEX = Utils.ACTIONIFY("torrent (.*)");

    private static final String SEPARATOR = "+";

    private static final String X1337_URL = "1337x.to/search/";
    private static final String X1337_POSTFIX = "/1/";

    private static final String RARBG_URL = "rarbg.to/torrents.php?search=";

    private static final String TORRENTZ_URL = "torrentz2.eu/search?f=";

    public TorrentSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();
        Matcher matcher = Pattern.compile(match).matcher(messageBody);

        if (match.equals(SEARCH_REGEX)) {
            if (matcher.find()) {
                String query = matcher.group(1);
                query = query.replaceAll("\\s+", SEPARATOR);
                String returnMessage = X1337_URL + query + X1337_POSTFIX
                        + "\n"
                        + RARBG_URL + query + "\n"
                        + TORRENTZ_URL + query;

                chatbot.sendMessage(returnMessage);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.equals(SEARCH_REGEX)) {
            return SEARCH_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.DEACTIONIFY(SEARCH_REGEX));
        return commands;
    }
}
