package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.hollandjake_api.helper.misc.Message;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.gabes_framework.core.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.core.hollandjake_api.helper.interfaces.Util.ACTIONIFY;

public class Shutdown extends ModuleBase {
    private final String SHUTDOWN_REGEX = ACTIONIFY("shutdown (\\d*)");
    private final String SHUTDOWN = ACTIONIFY("shutdown");

    public Shutdown(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(SHUTDOWN_REGEX, SHUTDOWN);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(SHUTDOWN)) {
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
            chatbot.sendMessage("No chyba cie pojebało");
            return false;
        }
        return false;
    }
}