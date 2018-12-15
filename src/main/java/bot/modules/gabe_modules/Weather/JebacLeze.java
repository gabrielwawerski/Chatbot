package bot.modules.gabe_modules.Weather;

import bot.Chatbot;
import bot.utils.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.modules_base.ResourceModule;
import bot.utils.helper_class.Message;

import java.util.List;

public class JebacLeze extends ResourceModule {

    public JebacLeze(Chatbot chatbot, List<String> commands, String responsesFile) {
        super(chatbot, commands, responsesFile);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        return false;
    }
}
