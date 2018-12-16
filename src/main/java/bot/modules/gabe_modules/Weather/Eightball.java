package bot.modules.gabe_modules.Weather;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.bot.helper_class.Message;
import bot.utils.gabe_modules.util.ResourceModule;

import java.util.List;

public class Eightball extends ResourceModule {
    public Eightball(Chatbot chatbot, List<String> commands, String responsesFile) {
        super(chatbot, commands, responsesFile);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }
}
