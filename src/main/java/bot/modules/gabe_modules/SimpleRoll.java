package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.gabe_modules.modules_base.BareModule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.utils.bot.helper.helper_interface.Util.ACTIONIFY;
import static bot.utils.bot.helper.helper_interface.Util.DEACTIONIFY;

public class SimpleRoll extends BareModule {
    private final String ROLL_PRESET_REGEX = ACTIONIFY("roll");
    private final String ROLL_REGEX = ACTIONIFY("roll (\\d+)");

    public SimpleRoll(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (match.equals(ROLL_PRESET_REGEX)) {
            int roll = roll(1, 101);
            chatbot.sendMessage("Twój los: " + Integer.toString(roll));
            return true;
        } else if (match.equals(ROLL_REGEX)) {
            Matcher matcher = Pattern.compile(ROLL_REGEX).matcher(message.getMessage());
            if (matcher.find()) {
                try {
                    int roll = roll(1, Integer.parseInt(matcher.group(1)));
                    chatbot.sendMessage("Twój los: " + roll);
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

    private int roll(int lower, int upper) {
        return (int) (Math.random() * (upper - lower) + lower);
    }

    private boolean isNumeric(String str) {
        return str.matches(".*\\d+.*");
    }
}
