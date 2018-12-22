package bot.modules.gabe_modules.work_in_progress;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;
import bot.gabes_framework.simple.SimpleModule;

import java.util.List;

public class ReloadTest extends SimpleModule {
    public ReloadTest(Chatbot chatbot, List<String> regexList) {
        super(chatbot, regexList);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : regexList) {
            if (match.equals(command)) {
                chatbot.reloadModules();
                return true;
            }
        }
        return false;
    }
}
