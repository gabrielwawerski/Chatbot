package bot.modules.gabe.util.search;

import bot.core.Chatbot;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.gabes_framework.core.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiTorrentSearch extends ModuleBase {
    private static final String X1337_URL = "1337x.to/search/";
    private static final String X1337_POSTFIX = "/1/";

    private static final String TORRENTZ_URL = "https://torrentz2.eu/search?f=";

    private final String TORRENT_REGEX = Utils.TO_REGEX("torrent (.*)");
    private final String T_REGEX = Utils.TO_REGEX("t (.*)");

    public MultiTorrentSearch(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (isOr(TORRENT_REGEX, T_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());

            if (matcher.find()) {
                addPoints(message, Utils.POINTS_MULTITORRENTSEARCH_REGEX);
                String userQuery = matcher.group(1).replaceAll("\\s+", "+");
                String messageToSend = X1337_URL + userQuery + X1337_POSTFIX
                            + "\n\n" + TORRENTZ_URL + userQuery;
                chatbot.sendMessage(messageToSend);
                return true;
            } else {
                chatbot.sendMessage("Coś żeś pojebał");
                return true;
            }
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(TORRENT_REGEX, T_REGEX);
    }
}
