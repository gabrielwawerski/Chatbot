package bot.modules.gabe_modules;

import bot.Chatbot;
import bot.utils.gabe_modules.util.SingleMessageModule;

import java.util.List;

public class Commands extends SingleMessageModule {
    public Commands(Chatbot chatbot, List<String> commands, String message) {
        super(chatbot, commands, message);
    }
}