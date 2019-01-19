package bot.modules.gabe.rand;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Config;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.helper.interfaces.Util;
import bot.core.gabes_framework.framework.resource.RandomResourceModule;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responds with randomly selected line from resource file.
 *
 * @author Gabe
 * @version 1.01
 *
 *
 */
public class EightBall extends RandomResourceModule {
    private static final String EIGHT_BALL_REGEX = Util.ACTIONIFY("8ball (.*)");
    private static final String ASK_REGEX = Util.ACTIONIFY("ask (.*)");
    private static final String EIGHT_REGEX = Util.ACTIONIFY("8 (.*)");

    public EightBall(Chatbot chatbot, String resourceName) {
        super(chatbot, resourceName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (isRegex()) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());

            if (matcher.find() && !matcher.group(1).isEmpty()) {
                addPoints(message, Config.POINTS_EIGHTBALL_REGEX);
                chatbot.sendMessage(Util.GET_RANDOM(resourceContents));
                return true;
            } else {
                throw new MalformedCommandException();
            }
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(EIGHT_BALL_REGEX, ASK_REGEX, EIGHT_REGEX);
    }
}
