package bot.utils.gabe_modules.util.module_library;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.core.helper.misc.Message;

import java.util.List;

import static bot.core.helper.interfaces.Util.GET_RANDOM;

/**
 * Easily create new modules if you only need following functionality: preset trigger commands and resourceContent that
 * will not change. This class takes care of API backend, if you only need mentioned functionality.<br>
 * Simply extend your module_library with this class, provide commands and resourceContent in the constructor and you're set!
 *
 * @version 1.0
 * @author Gabe
 */
public abstract class MessageModule extends SimpleModule {
    protected List<String> messages;

    /**
     * Provides an easy way of getting the bot to respond to command/s. If there is more that one message, it gets
     * picked at random.
     *
     * @param chatbot chatbot reference
     * @param commands trigger commands for your module_library
     * @param messages message, or multiple messages (picked at random) to send after trigger is found
     * @author Gabe
     */
    public MessageModule(Chatbot chatbot, List<String> commands, List<String> messages) {
        super(chatbot, commands);
        this.messages = messages;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : commands) {
            if (match.equals(command)) {
                chatbot.sendMessage(GET_RANDOM(messages));
                return true;
            }
        }
        return false;
    }
}
