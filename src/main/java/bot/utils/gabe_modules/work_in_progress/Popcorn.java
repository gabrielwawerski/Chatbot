package bot.utils.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.helper.misc.Message;
import bot.utils.gabe_modules.module_library.SimpleModule;

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
