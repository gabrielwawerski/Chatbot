package bot.modules.gabe.util.info;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.message.SingleMessageModule;

import java.util.List;

/**
 * @since v0.3310
 */
public class ATG extends SingleMessageModule {
    public ATG(Chatbot chatbot, String message) {
        super(chatbot, message);
    }

    @Override
    protected List<String> setRegexes() {
        return getMappedRegexes("atg");
    }
}
