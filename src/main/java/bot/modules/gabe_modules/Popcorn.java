package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.bot.helper.helper_class.Message;
import bot.utils.gabe_modules.util.ModuleBase;

import java.util.List;

public class Popcorn extends ModuleBase {
    public Popcorn(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    @Override
    public boolean process(Message message) {
        updateMatch(message);

        for (String command : commands) {
            if (match.equals(command)) {
                chatbot.sendImageFromURLWithMessage("https://i.imgur.com/0hQyd5L.gif", "\uD83C\uDF7F");
                return true;
            }
        }
        return false;
    }
}
