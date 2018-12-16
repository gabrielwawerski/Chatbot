package bot.utils.gabe_modules.util;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.modules_base.BaseModule;
import bot.utils.bot.helper_class.Message;

import java.util.List;

import static bot.utils.bot.helper_interface.Util.GET_RANDOM;

/**
 * Easily create new modules if you only need following functionality: preset trigger commands and resourceName that
 * will not change. This class takes care of API backend, if you only need mentioned functionality.<br>
 * Simply extend your module with this class, provide commands and resourceName in the constructor and you're set!
 *
 * @version 1.0
 * @author Gabe
 */
public class MessageModule extends BaseModule {
    private List<String> messages;

    /**
     * Provides an easy way of getting the bot to respond to command/s. If there is more that one message, it gets
     * picked at random.
     *
     * @param chatbot
     * @param commands
     * @param messages
     * @author Gabe
     */
    public MessageModule(Chatbot chatbot, List<String> commands, List<String> messages) {
        super(chatbot, commands);
        this.messages = messages;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        String match = getMatch(message);
        for (String command : commands) {
            if (match.equals(command)) {
                chatbot.sendMessage(GET_RANDOM(messages));
                return true;
            }
        }
        return false;
    }

    @Override
    public String getMatch(Message message) {
        return "";
    }
}
