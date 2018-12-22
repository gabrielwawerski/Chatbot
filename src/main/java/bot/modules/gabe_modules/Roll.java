package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.helper.interfaces.Util.ACTIONIFY;
import static bot.core.helper.interfaces.Util.DEACTIONIFY;

/**
 * @author Gabe
 */
public class Roll extends ModuleBase {
    private final String ROLL_PRESET_REGEX = ACTIONIFY("roll");
    private final String ROLL_REGEX = ACTIONIFY("roll (\\d+)");

    private final int MIN_ROLL = 3;
    private final int MAX_ROLL = 100 + 1;

    public Roll(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(ROLL_PRESET_REGEX)) {
            int roll = roll(MIN_ROLL, MAX_ROLL);
            chatbot.sendMessage("Twój los: " + Integer.toString(roll));
            return true;
        } else if (match.equals(ROLL_REGEX)) {
            Matcher matcher = Pattern.compile(ROLL_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                try {
                    int roll = roll(MIN_ROLL, Integer.parseInt(matcher.group(1)));
                    chatbot.sendMessage("Twój los: " + roll + " " + Utils.EMOJI_HOURGLASS);
                } catch (NumberFormatException e) {
                    chatbot.sendMessage("Coś poszło nie tak... gdzie jest moje gabe?");
                    throw new MalformedCommandException();
//                    e.printStackTrace();
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

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();

        if (messageBody.matches(ROLL_PRESET_REGEX)) {
            return ROLL_PRESET_REGEX;
        } else if (messageBody.matches(ROLL_REGEX)) {
            return ROLL_REGEX;
        } else {
            return "";
        }
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(ROLL_PRESET_REGEX));
        commands.add(DEACTIONIFY(ROLL_REGEX));
        return commands;
    }

    /**
     * @author hollandjake
     */
    private int roll(int lower, int upper) {
        return (int) (Math.random() * (upper - lower) + lower);
    }

    private boolean isNumeric(String str) {
        return str.matches(".*\\d+.*");
    }
}
