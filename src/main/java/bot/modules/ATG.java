package bot.modules;

import bot.core.Chatbot;
import bot.gabes_framework.message.SingleMessageModule;

import java.util.List;

public class ATG extends SingleMessageModule {
    public ATG(Chatbot chatbot, List<String> regexes, String message) {
        super(chatbot, regexes, message);
    }
}
