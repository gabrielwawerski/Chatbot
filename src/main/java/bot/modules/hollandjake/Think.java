package bot.modules.hollandjake;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.core.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.util.module_library.SimpleModule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.helper.interfaces.Util.ACTIONIFY;
import static bot.core.helper.interfaces.Util.DEACTIONIFY;

public class Think extends SimpleModule {
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
        if (match.equals(THINK_REGEX) || match.equals(THONK_REGEX)) {
            chatbot.sendMessage("\uD83E\uDD14");
            return true;
        } else if (match.equals(MULTI_THINK_REGEX) || match.equals(MULTI_THONK_REGEX)) {
            Matcher matcher = Pattern.compile(match).matcher(message.getMessage());
            if (matcher.find()) {
                int repeats = Integer.parseInt(matcher.group(1));
                if (repeats > 100) {
                    chatbot.sendMessage("Nie zamyśl sie debilu");
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
    @SuppressWarnings("Duplicates")
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(THINK_REGEX)) {
            return THINK_REGEX;
        } else if (messageBody.matches(THONK_REGEX)) {
            return THONK_REGEX;
        } else if (messageBody.matches(MULTI_THINK_REGEX)) {
            return MULTI_THINK_REGEX;
        } else if (messageBody.matches(MULTI_THONK_REGEX)) {
            return MULTI_THONK_REGEX;
        } else {
            return "";
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(THINK_REGEX));
        commands.add(DEACTIONIFY(THONK_REGEX));
        commands.add(DEACTIONIFY(MULTI_THINK_REGEX));
        commands.add(DEACTIONIFY(MULTI_THONK_REGEX));
        return commands;
    }
}