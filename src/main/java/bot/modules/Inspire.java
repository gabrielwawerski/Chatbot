package bot.modules;

import bot.Chatbot;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.bot.helper.helper_interface.Module;
import bot.utils.gabe_modules.modules_base.ModuleBase;

import java.util.ArrayList;

import static bot.utils.bot.helper.helper_interface.Util.*;

public class Inspire extends ModuleBase {
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