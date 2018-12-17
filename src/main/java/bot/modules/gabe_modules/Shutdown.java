package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.util.BareModule;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.utils.bot.helper.helper_interface.Util.ACTIONIFY;
import static bot.utils.bot.helper.helper_interface.Util.DEACTIONIFY;

public class Shutdown extends BareModule {
    private final String SHUTDOWN_REGEX = ACTIONIFY("shutdown (\\d*)");

    public Shutdown(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        String match = getMatch(message);
        if (match.equals(SHUTDOWN_REGEX)) {
            Matcher matcher = Pattern.compile(SHUTDOWN_REGEX).matcher(message.getMessage());
            if (matcher.find() && matcher.group(1).equals(chatbot.getShutdownCode())) {
                chatbot.quit();
                return true;
            } else {
                chatbot.sendMessage("No chyba cie pojebalo");
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(SHUTDOWN_REGEX)) {
            return SHUTDOWN_REGEX;
        } else {
            return "";
        }
    }

    @Override
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(SHUTDOWN_REGEX));
        return commands;
    }
}