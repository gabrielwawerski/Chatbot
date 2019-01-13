package bot.modules.gabe.image;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Text;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.hollandjake_api.helper.interfaces.Util.ACTIONIFY;

/**
 * @version 1.1
 */
public class Think extends ModuleBase {
    private final String THINK_REGEX = ACTIONIFY("think");
    private final String MULTI_THINK_REGEX = ACTIONIFY("think (\\d*)");
    private final String THONK_REGEX = ACTIONIFY("thonk");
    private final String MULTI_THONK_REGEX = ACTIONIFY("thonk (\\d*)");

    public Think(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        String match = getMatch(message);
        if (isOr(THINK_REGEX, THONK_REGEX)) {
            chatbot.sendMessage("\uD83E\uDD14");
            return true;
        } else if (isOr(MULTI_THINK_REGEX, MULTI_THONK_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());

            if (matcher.find()) {
                int repeats = Integer.parseInt(matcher.group(1));

                if (repeats > 100) {
                    chatbot.sendMessage(Text.THINK_TOO_MUCH_REQUESTED);
                } else {
                    chatbot.sendMessage(new String(new char[repeats]).replace("\0", "\uD83E\uDD14"));
                }
            } else {
                throw new MalformedCommandException();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(
                THINK_REGEX,
                MULTI_THINK_REGEX,
                THONK_REGEX,
                MULTI_THONK_REGEX);
    }
}