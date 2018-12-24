package bot.modules.gabe_modules;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.core.exceptions.MalformedCommandException;
import bot.gabes_framework.core.ModuleBase;
import bot.gabes_framework.core.libs.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.helper.interfaces.Util.ACTIONIFY;
import static bot.core.helper.interfaces.Util.DEACTIONIFY;

public class Shutdown extends ModuleBase {
    private final String SHUTDOWN_REGEX = ACTIONIFY("shutdown (\\d*)");
    private final String SHUTDOWN = ACTIONIFY("shutdown");

    public Shutdown(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        String match = getMatch(message);
        if (match.equals(SHUTDOWN)) {
            if (message.getSender().getName().equals("Gabriel Wawerski")) {
                chatbot.quit();
                return true;
            } else {
                chatbot.sendMessage("No chyba cie pojebało");
                return false;
            }
        }

        Matcher matcher = Pattern.compile(SHUTDOWN_REGEX).matcher(message.getMessage());
        if (matcher.find() && matcher.group(1).equals(chatbot.getShutdownCode())) {
            chatbot.quit();
            return true;
        } else if (matcher.find() && !matcher.group(1).equals(chatbot.getShutdownCode())){
            chatbot.sendMessage("No chyba cie pojebalo");
            return false;
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return findMatch(message, SHUTDOWN_REGEX, SHUTDOWN);
    }

    @Override
    public ArrayList<String> getCommands() {
        return Utils.getCommands(SHUTDOWN_REGEX, SHUTDOWN);
    }
}