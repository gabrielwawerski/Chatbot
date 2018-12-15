package bot.modules.gabe_modules.Weather;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper_class.Message;
import bot.utils.gabe_modules.modules_base.BaseModule;

import java.util.List;

public class Eightball extends BaseModule {
    public Eightball(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }
}
