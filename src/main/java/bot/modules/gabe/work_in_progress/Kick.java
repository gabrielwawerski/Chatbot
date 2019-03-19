package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.framework.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.List;

public class Kick extends ModuleBase {
    private static final String REGEX_KICK = Utils.TO_REGEX("kick (.*)");

    public Kick(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    protected List<String> setRegexes() {
        return null;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }
}
