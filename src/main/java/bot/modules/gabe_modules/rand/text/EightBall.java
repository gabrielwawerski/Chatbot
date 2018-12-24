package bot.modules.gabe_modules.rand.text;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.core.helper.interfaces.Util;
import bot.gabes_framework.util.resource.RandomResourceModule;

import java.util.ArrayList;
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
    private final String EIGHT_BALL_REGEX = Util.ACTIONIFY("8ball (.*)");
    private final String ASK_REGEX = Util.ACTIONIFY("ask (.*)");
    private final String EIGHT_REGEX = Util.ACTIONIFY("8 (.*)");

    public EightBall(Chatbot chatbot, String resourceName) {
        super(chatbot, resourceName);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(EIGHT_BALL_REGEX) || match.equals(ASK_REGEX)
                || match.equals(EIGHT_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());

            if (matcher.find() && !matcher.group(1).isEmpty()) {
                chatbot.sendMessage(Util.GET_RANDOM(resourceContent));
            } else {
                throw new MalformedCommandException();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(EIGHT_BALL_REGEX)) {
            return EIGHT_BALL_REGEX;
        } else if (messageBody.matches(ASK_REGEX)) {
            return ASK_REGEX;
        } else if (messageBody.matches(EIGHT_REGEX)) {
            return EIGHT_REGEX;
        } else {
            return "";
        }
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(Util.DEACTIONIFY(EIGHT_BALL_REGEX));
        commands.add(Util.DEACTIONIFY(ASK_REGEX));
        commands.add(Util.DEACTIONIFY(EIGHT_REGEX));
        return commands;
    }
}
