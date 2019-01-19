package bot.modules.gabe.rand;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Config;
import bot.core.gabes_framework.core.util.Emoji;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.gabes_framework.framework.ModuleBase;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.hollandjake_api.helper.interfaces.Util.ACTIONIFY;

/**
 * @author Gabe
 */
public class Roll extends ModuleBase {
    private final int MIN_ROLL = 3;
    private final int MAX_ROLL = 100 + 1;

    private static final String ROLL_FIXED_REGEX = ACTIONIFY("roll");
    private static final String ROLL_REGEX = ACTIONIFY("roll (\\d+)");

    public Roll(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(ROLL_FIXED_REGEX, ROLL_REGEX);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(ROLL_FIXED_REGEX)) {
            addPoints(message, Config.POINTS_ROLL_SIMPLE_REGEX);
            int roll = roll(MIN_ROLL, MAX_ROLL);
            chatbot.sendMessage("Twój los: " + Integer.toString(roll));
            return true;

        } else if (is(ROLL_REGEX)) {
            Matcher matcher = Pattern.compile(ROLL_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                addPoints(message, Config.POINTS_ROLL_REGEX);
                try {
                    int roll = roll(MIN_ROLL, Integer.parseInt(matcher.group(1)));
                    chatbot.sendMessage("Twój los: " + roll + " " + Emoji.EMOJI_HOURGLASS);
                } catch (NumberFormatException e) {
                    chatbot.sendMessage("Coś poszło nie tak... gdzie jest moje gabe?");
                    throw new MalformedCommandException();
                }
            } else {
                chatbot.sendMessage("Coś poszło nie tak... gdzie jest moje gabe?");
                throw new MalformedCommandException();
            }
            return true;
        } else {
            return false;
        }
    }

    private int roll(int lower, int upper) {
        return (int) (Math.random() * (upper - lower) + lower);
    }

    private boolean isNumeric(String str) {
        return str.matches(".*\\d+.*");
    }
}
