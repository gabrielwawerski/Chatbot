package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.gabes_framework.helper.message.SingleMessageModule;

import java.util.List;

/**
 * @since v0.3310
 */
public class ATG extends SingleMessageModule {
    public ATG(Chatbot chatbot, List<String> regexes, String message) {
        super(chatbot, regexes, message);
    }
}
