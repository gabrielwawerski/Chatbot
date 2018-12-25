package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.util.message.SingleMessageModule;

import java.util.List;

public class ATG extends SingleMessageModule {
    public ATG(Chatbot chatbot, List<String> regexes, String message) {
        super(chatbot, regexes, message);
    }
}
