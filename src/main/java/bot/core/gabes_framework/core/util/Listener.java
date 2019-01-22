package bot.core.gabes_framework.core.util;

import bot.core.Chatbot;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

/**
 * performs when logOnly is active
 */
public class Listener extends ModuleBase {
    public Listener(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return null;
    }
}
