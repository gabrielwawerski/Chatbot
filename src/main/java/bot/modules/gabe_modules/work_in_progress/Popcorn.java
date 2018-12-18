package bot.modules.gabe_modules.work_in_progress;

import bot.Chatbot;
import bot.impl.orig_impl.helper.misc.Message;
import bot.impl.gabes_framework.simple.SimpleModule;

import java.util.List;

public class Popcorn extends SimpleModule {
    public Popcorn(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    @Override
    public boolean process(Message message) {
        updateMatch(message);

        for (String command : regexList) {
            if (match.equals(command)) {
                chatbot.sendImageFromURLWithMessage("https://i.imgur.com/0hQyd5L.gif", "\uD83C\uDF7F");
                return true;
            }
        }
        return false;
    }
}
