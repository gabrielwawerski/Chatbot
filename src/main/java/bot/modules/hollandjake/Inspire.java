package bot.modules.hollandjake;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.gabes_framework.simple.SimpleModule;

import java.util.ArrayList;

import static bot.core.helper.interfaces.Util.*;

public class Inspire extends SimpleModule {
    private final String INSPIRE_REGEX = ACTIONIFY("inspire");

    public Inspire(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean process(Message message) {
        String match = getMatch(message);
        if (match.equals(INSPIRE_REGEX)) {
            String imgURL = GET_PAGE_SOURCE("http://inspirobot.me/api?generate=true");
            chatbot.sendImageFromURLWithMessage(imgURL, "Inspiruje..");
            return true;
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(INSPIRE_REGEX)) {
            return INSPIRE_REGEX;
        } else {
            return "";
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(INSPIRE_REGEX));
        return commands;
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }
}