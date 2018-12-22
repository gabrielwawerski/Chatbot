package bot.modules.gabe_modules.query;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;
import fr.plaisance.bitly.Bit;
import fr.plaisance.bitly.Bitly;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentTest extends ModuleBase {
    private static final String X1337_URL = "1337x.to/search/";
//    private static final String X1337_URL = "rarbg.to/torrents.php?search=";
    private static final String POSTFIX = "/1/";

    private static final String TORRENTZ_URL = "https://torrentz2.eu/search?f=";

    private final String SEARCH_REGEX = Utils.TO_REGEX("torrent (.*)");
    private final Bitly bitly;

    private static final String BITLY_ACCESS_TOKEN = "ccbb8945fa671a57a48645c181466d9ad5619749";

    public TorrentTest(Chatbot chatbot) {
        super(chatbot);

        bitly = Bit.ly(BITLY_ACCESS_TOKEN);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);
        String messageBody = message.getMessage();

        if (match.equals(SEARCH_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());

            if (matcher.find()) {
                String userQuery = matcher.group(1).replaceAll("\\s+", "+");
                String messageToSend = X1337_URL + userQuery + POSTFIX
                            + "\n" + TORRENTZ_URL + userQuery;

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
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(SEARCH_REGEX)) {
            return SEARCH_REGEX;
        }
        return "";
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Utils.TO_COMMAND(SEARCH_REGEX));
        return commands;
    }
}
