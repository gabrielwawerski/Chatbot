package bot.modules.gabe.work_in_progress;

import bot.core.Chatbot;
import bot.core.gabes_framework.core.util.Utils;
import bot.core.gabes_framework.helper.ModuleBase;
import bot.core.hollandjake_api.exceptions.MalformedCommandException;
import bot.core.hollandjake_api.helper.misc.Message;

import java.util.ArrayList;
import java.util.List;

public class Mention extends ModuleBase {
    private final String MENTION_REGEX = Utils.TO_REGEX("mention");

    public Mention(Chatbot chatbot) {
        super(chatbot);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        if (is(MENTION_REGEX)) {
            chatbot.sendMentionMessage("Gabriel Wawerski", "test");
            return true;
        }
        return false;
    }

    @Override
    protected List<String> setRegexes() {
        return List.of(MENTION_REGEX);
    }
}
